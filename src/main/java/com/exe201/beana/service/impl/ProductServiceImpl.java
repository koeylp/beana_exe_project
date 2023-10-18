package com.exe201.beana.service.impl;

import com.exe201.beana.dto.ProductDto;
import com.exe201.beana.dto.ProductImageListDto;
import com.exe201.beana.dto.ProductRequestDto;
import com.exe201.beana.dto.ProductRequestFilterDto;
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
import java.util.*;
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
        imageCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(imageCookie);
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
        Collections.sort(sortedProductList, Comparator.comparing(ProductDto::getTimeCreated).reversed());
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
    public ProductDto editProduct(ProductRequestDto productRequest, Long productId, HttpServletRequest request, HttpServletResponse response) {
        Optional<Product> foundProduct = productRepository.findProductByStatusAndId((byte) 1, productId);
        if (foundProduct.isEmpty())
            throw new ResourceNotFoundException("Product not found with id: " + productId);

        Optional<Reputation> foundReputation = reputationRepository.findReputationByStatusAndId((byte) 1, productRequest.getReputationId());
        if (foundReputation.isEmpty())
            throw new ResourceNotFoundException("Reputation does not exist with id: " + productRequest.getReputationId());

        Optional<ChildCategory> foundChildCategory = childCategoryRepository.findChildCategoryByStatusAndId((byte) 1, productRequest.getChildCategoryId());
        if (foundChildCategory.isEmpty())
            throw new ResourceNotFoundException("Child category does not exist with id: " + productRequest.getChildCategoryId());

        Product newProduct = getProduct(productRequest, foundChildCategory, foundReputation);
        productRepository.save(newProduct);

        List<ProductSkin> productSkinList = new ArrayList<>();

        for (Long skinId : productRequest.getSkinIds()) {
            ProductSkin newProductSkin = new ProductSkin();
            Optional<Skin> foundSkin = skinRepository.findSkinByStatusAndId((byte) 1, skinId);
            if (foundSkin.isEmpty())
                throw new ResourceNotFoundException("Skin not found with id: " + skinId);
            newProductSkin.setProduct(newProduct);
            newProductSkin.setSkin(foundSkin.get());
            newProductSkin.setStatus((byte) 1);
            productSkinList.add(newProductSkin);
        }
//        productSkinRepository.saveAll(productSkinList);
//
//        newProduct.setProductSkins(productSkinList);
//        foundProduct.get().setName(productRequest.getName());
//        foundProduct.get().setPrice(productRequest.getPrice());
//        foundProduct.get().setReputation(foundReputation.get());
//        foundProduct.get().setProductSkins(productSkinList);
//        foundProduct.get().setIngredients(productRequest.getIngredients());
//        foundProduct.get().setSpecification(productRequest.getSpecification());
//        foundProduct.get().setStatus((byte) 1);
//        foundProduct.get().setMainFunction(productRequest.getMainFunction());
//        foundProduct.get().setHowToUse(productRequest.getHowToUse());
//        foundProduct.get().setProductImageList();

        // check image list already uploaded
        ProductImageListDto productImageList = getImageFromCookie(request);

        // get image uploaded from cookie and save to database
        newProduct.setProductImageList(productImageList.getProductImageList());

        for (ProductImage productImage : productImageList.getProductImageList()) {
            productImage.setProduct(newProduct);
            productImageRepository.save(productImage);
        }

        // clear images after done saving
        productImageList.getProductImageList().clear();
        saveImageToCookie(productImageList, response);


        return ProductMapper.INSTANCE.toProductDto(productRepository.save(foundProduct.get()));
    }

    @Override
    public List<ProductDto> getProductsByPriceRange(double startPrice, double endPrice) {
        List<ProductDto> sortedProductList = new ArrayList<>(productRepository.findAllByStatusAndPriceBetween((byte) 1, startPrice, endPrice).stream().map(ProductMapper.INSTANCE::toProductDto).toList());
        Collections.sort(sortedProductList, Comparator.comparing(ProductDto::getPrice));
        return sortedProductList;
    }

    @Override
    public List<ProductDto> filterProductList(String sortType, String category, String childCategory,
                                              String skin, String status, String startPrice, String endPrice) {


        /* sortType....
         0 : "Mới nhất"
         1 : "Bán chạy nhất"
         2 : "Giá từ thấp đến cao"
         3 : "Giá từ cao đến thấp" */


        if (status == null)
            status = String.valueOf(1);
        if (endPrice == null)
            endPrice = String.valueOf(Double.MAX_VALUE);
        List<ProductDto> tempList = new ArrayList<>(productRepository.findAllByStatusAndPriceBetween(Byte.parseByte(status),
                Double.parseDouble(startPrice), Double.parseDouble(endPrice)).stream().map(ProductMapper.INSTANCE::toProductDto).toList());

        List<ProductDto> sortedProductList = new ArrayList<>();

        // Categories
        String[] categories = category.split(",");
        if (!category.isEmpty()) {
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

        }

        String[] skins = skin.split(",");
        if (!skin.isEmpty()) {

        }


        //

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


}
