package com.exe201.beana.service.impl;

import com.exe201.beana.dto.*;
import com.exe201.beana.entity.*;
import com.exe201.beana.exception.ResourceAlreadyExistsException;
import com.exe201.beana.exception.ResourceNotFoundException;
import com.exe201.beana.mapper.ChildCategoryMapper;
import com.exe201.beana.mapper.ProductMapper;
import com.exe201.beana.repository.*;
import com.exe201.beana.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductSkinRepository productSkinRepository;
    private final ChildCategoryRepository childCategoryRepository;
    private final ReputationRepository reputationRepository;
    private final SkinRepository skinRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private static final String IMAGE_COOKIE_NAME = "IMAGE_COOKIE";


    @Override
    public ProductDto addProduct(ProductRequestDto productRequest, HttpServletRequest request, HttpServletResponse response) {
        // check product exits
        Optional<Product> foundProduct = productRepository.findProductByStatusAndName((byte) 1, productRequest.getName());
        if (foundProduct.isPresent())
            throw new ResourceAlreadyExistsException("Product exists with id: " + foundProduct.get().getId());

        // check child category exists
        Optional<ChildCategory> foundChildCategory = childCategoryRepository.findChildCategoryByStatusAndId((byte) 1, productRequest.getChildCategoryId());
        if (foundChildCategory.isEmpty())
            throw new ResourceNotFoundException("Child category does not exist with id: " + productRequest.getChildCategoryId());

        // check reputation exist
        Optional<Reputation> foundReputation = reputationRepository.findReputationByStatusAndId((byte) 1, productRequest.getReputationId());
        if (foundReputation.isEmpty())
            throw new ResourceNotFoundException("Reputation does not exist with id: " + productRequest.getReputationId());

        // check image list already uploaded
        ProductImageListDto productImageList = getImageFromCookie(request);
        if (productImageList.getProductImageList() == null || productImageList.getProductImageList().isEmpty())
            throw new ResourceNotFoundException("Upload Images first!");

        // save product first to get id identity
        Product newProduct = getProduct(productRequest, foundChildCategory, foundReputation);
        productRepository.save(newProduct);

        // get product just saved
        foundProduct = productRepository.findProductByStatusAndName((byte) 1, productRequest.getName());
        if (foundProduct.isPresent())
            newProduct = foundProduct.get();

        // skin adding data
        List<ProductSkin> productSkinList = new ArrayList<>();

        for (Long skinId : productRequest.getSkinIds()) {
            Optional<Skin> foundSkin = skinRepository.findSkinByStatusAndId((byte) 1, skinId);
            if (foundSkin.isEmpty())
                throw new ResourceNotFoundException("Skin not found with id: " + skinId);
            ProductSkin newProductSkin = new ProductSkin(null, newProduct, foundSkin.get(), (byte) 1);
            productSkinRepository.save(newProductSkin);
            productSkinList.add(newProductSkin);
        }
        newProduct.setProductSkins(productSkinList);

        // get image uploaded from cookie and save to database
        newProduct.setProductImageList(productImageList.getProductImageList());

        for (ProductImage productImage : productImageList.getProductImageList()) {
            productImage.setProduct(newProduct);
            productImageRepository.save(productImage);
        }

        // clear images after done saving
        productImageList.getProductImageList().clear();
        saveImageToCookie(productImageList, response);

        return ProductMapper.INSTANCE.toProductDto(productRepository.save(newProduct));
    }

    private ProductImageListDto getImageFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(IMAGE_COOKIE_NAME)) {
                    return deserializeImages(cookie.getValue());
                }
            }
        }
        return new ProductImageListDto();
    }

    private ProductImageListDto deserializeImages(String imageJson) {
        try {
            String decodedImageData = URLDecoder.decode(imageJson, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(decodedImageData, ProductImageListDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveImageToCookie(ProductImageListDto imageUrls, HttpServletResponse response) {
        Cookie imageCookie = new Cookie(IMAGE_COOKIE_NAME, serializeImageList(imageUrls));
        imageCookie.setMaxAge(10 * 60);
        response.addCookie(imageCookie);
    }

    private void clearCookie(ProductImageListDto imageUrls, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(IMAGE_COOKIE_NAME)) {
                    cookie.setValue(serializeImageList(imageUrls));
                    response.addCookie(cookie);
                }
            }
        }
    }

    private String serializeImageList(ProductImageListDto imageUrls) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return URLEncoder.encode(objectMapper.writeValueAsString(imageUrls), StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static Product getProduct(ProductRequestDto productRequest, Optional<ChildCategory> foundChildCategory, Optional<Reputation> foundReputation) {
        Product newProduct = new Product();
        newProduct.setName(productRequest.getName());
        newProduct.setQuantity(productRequest.getQuantity());
        newProduct.setPrice(productRequest.getPrice());
        newProduct.setDescription(productRequest.getDescription());
        newProduct.setMainFunction(productRequest.getMainFunction());
        newProduct.setIngredients(productRequest.getIngredients());
        newProduct.setSpecification(productRequest.getSpecification());
        newProduct.setSoldQuantity(0);
        newProduct.setStatus((byte) 1);
        newProduct.setCertification(productRequest.getCertification());
        newProduct.setChildCategory(foundChildCategory.get());
        newProduct.setReputation(foundReputation.get());
        newProduct.setHowToUse(productRequest.getHowToUse());
        return newProduct;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<ProductDto> sortedProductList = new ArrayList<>(productRepository.findAllByStatus((byte) 1).stream().map(ProductMapper.INSTANCE::toProductDto).toList());
        sortedProductList.sort(Comparator.comparing(ProductDto::getTimeCreated).reversed());
        return sortedProductList;
    }

    @Override
    public List<ProductDto> getProductsByChildCategoryId(Long childCategoryId) {
        Optional<ChildCategory> childCategory = childCategoryRepository.findChildCategoryByStatusAndId((byte) 1, childCategoryId);
        if (childCategory.isEmpty())
            throw new ResourceNotFoundException("Child category does not exist with id: " + childCategoryId);
        return productRepository.findAllByStatusAndChildCategory((byte) 1, childCategory.get()).stream().map(ProductMapper.INSTANCE::toProductDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto editProduct(ProductEditRequestDto productRequest, Long productId, HttpServletRequest request, HttpServletResponse response) {

        // check the existence of product
        Optional<Product> foundProduct = productRepository.findById(productId);
        if (foundProduct.isEmpty())
            throw new ResourceNotFoundException("Product not found with id: " + productId);

        // check the existence of reputation
        if (productRequest.getReputationId() != null) {
            Optional<Reputation> foundReputation = reputationRepository.findReputationByStatusAndId((byte) 1, productRequest.getReputationId());
            if (foundReputation.isEmpty())
                throw new ResourceNotFoundException("Reputation does not exist with id: " + productRequest.getReputationId());
        }

        // check the existence of child category
        if (productRequest.getChildCategoryId() != null) {
            Optional<ChildCategory> foundChildCategory = childCategoryRepository.findChildCategoryByStatusAndId((byte) 1, productRequest.getChildCategoryId());
            if (foundChildCategory.isEmpty())
                throw new ResourceNotFoundException("Child category does not exist with id: " + productRequest.getChildCategoryId());
        }

        if (productRequest.getName() != null)
            foundProduct.get().setName(productRequest.getName());

        if (productRequest.getQuantity() != -1)
            foundProduct.get().setQuantity(productRequest.getQuantity());

        if (productRequest.getPrice() != -1)
            foundProduct.get().setPrice(productRequest.getPrice());

        if (productRequest.getCertification() != null)
            foundProduct.get().setDescription(productRequest.getDescription());

        if (productRequest.getMainFunction() != null)
            foundProduct.get().setMainFunction(productRequest.getMainFunction());

        if (productRequest.getIngredients() != null)
            foundProduct.get().setIngredients(productRequest.getIngredients());

        if (productRequest.getHowToUse() != null)
            foundProduct.get().setHowToUse(productRequest.getHowToUse());

        if (productRequest.getCertification() != null)
            foundProduct.get().setCertification(productRequest.getCertification());

        if (productRequest.getSpecification() != null)
            foundProduct.get().setSpecification(productRequest.getSpecification());

        // check image list already uploaded
        ProductImageListDto productImageList = getImageFromCookie(request);

        if (!productImageList.getProductImageList().isEmpty()) {
            for (ProductImage productImage : productImageList.getProductImageList()) {
                productImage.setProduct(foundProduct.get());
                productImageRepository.save(productImage);
            }

            productImageList.getProductImageList().addAll(foundProduct.get().getProductImageList());
            // get image uploaded from cookie and save to database
            foundProduct.get().setProductImageList(productImageList.getProductImageList());

            // clear images after done saving
            productImageList.getProductImageList().clear();
            saveImageToCookie(productImageList, response);
        }

        return ProductMapper.INSTANCE.toProductDto(productRepository.save(foundProduct.get()));
    }


    @Override
    public List<ProductDto> filterProductList(String sortType, String category, String childCategory,
                                              String skin, String status, String startPrice, String endPrice) {

        /* sortType....
         0 : "Mới nhất"
         1 : "Bán chạy nhất"
         2 : "Giá từ thấp đến cao"
         3 : "Giá từ cao đến thấp" */

        // check null and assign value
        if (status == null)
            status = String.valueOf(1);
        if (startPrice == null)
            startPrice = String.valueOf(0);
        if (endPrice == null)
            endPrice = String.valueOf(Double.MAX_VALUE);


        List<ProductDto> tempList = new ArrayList<>(productRepository.findAllByStatusAndPriceBetween(Byte.parseByte(status),
                Double.parseDouble(startPrice), Double.parseDouble(endPrice)).stream().map(ProductMapper.INSTANCE::toProductDto).toList());

        List<ProductDto> sortedProductList = new ArrayList<>();

        int count = -1;

        // Categories
        String[] categories = category.split(",");
        if (!category.isEmpty()) {
            count++;
            for (String categoryName : categories) {
                for (ProductDto product : tempList) {
                    Optional<Category> foundCategory = categoryRepository.findCategoryByStatusAndChildCategoriesContaining((byte) 1,
                            ChildCategoryMapper.INSTANCE.toChildCategory(product.getChildCategory()));
                    if (foundCategory.isPresent() && categoryName.equalsIgnoreCase(foundCategory.get().getName())) {
                        sortedProductList.add(product);
                    }
                }

            }
        }

        // Child Categories
        String[] childCategories = childCategory.split(",");
        if (!childCategory.isEmpty()) {
            count++;
            for (String childCategoryName : childCategories) {
                for (ProductDto product : tempList) {
                    if (childCategoryName.equalsIgnoreCase(product.getChildCategory().getName()))
                        sortedProductList.add(product);
                }
            }
        }

        String[] skins = skin.split(",");
        if (!skin.isEmpty()) {
            count++;
            for (String skinName : skins) {
                for (ProductDto product : tempList) {
                    for (ProductSkinDto productSkinDto : product.getProductSkins()) {
                        if (productSkinDto.getSkin().getName().equalsIgnoreCase(skinName))
                            sortedProductList.add(product);
                    }
                }
            }
        }

        if (count == -1)
            sortedProductList.addAll(tempList);


        if (sortType.equalsIgnoreCase("moi-nhat")) {
            sortedProductList.sort(Comparator.comparing(ProductDto::getTimeCreated).reversed());
        } else if (sortType.equalsIgnoreCase("ban-chay-nhat")) {
            sortedProductList.sort(Comparator.comparing(ProductDto::getSoldQuantity).reversed());
        } else if (sortType.equalsIgnoreCase("gia-tu-thap-den-cao")) {
            sortedProductList.sort(Comparator.comparing(ProductDto::getPrice));
        } else if (sortType.equalsIgnoreCase("gia-tu-cao-den-thap")) {
            sortedProductList.sort(Comparator.comparing(ProductDto::getPrice).reversed());
        }
        return sortedProductList;
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Optional<Product> foundProduct = productRepository.findProductByStatusAndId((byte) 1, productId);
        if (foundProduct.isEmpty())
            throw new ResourceNotFoundException("Product not found with id:" + productId);
        return ProductMapper.INSTANCE.toProductDto(foundProduct.get());
    }


}
