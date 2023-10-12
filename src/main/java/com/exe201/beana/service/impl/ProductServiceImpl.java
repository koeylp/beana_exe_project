package com.exe201.beana.service.impl;

import com.exe201.beana.dto.ProductDto;
import com.exe201.beana.dto.ProductImageListDto;
import com.exe201.beana.dto.ProductRequestDto;
import com.exe201.beana.entity.*;
import com.exe201.beana.exception.ResourceNameAlreadyExistsException;
import com.exe201.beana.exception.ResourceNotFoundException;
import com.exe201.beana.mapper.ProductMapper;
import com.exe201.beana.repository.*;
import com.exe201.beana.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
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
    private static final String IMAGE_COOKIE_NAME = "IMAGE_COOKIE";

    @Override
    public ProductDto addProduct(ProductRequestDto productRequest, HttpServletRequest request) {
        Optional<Product> foundProduct = productRepository.findProductByStatusAndName((byte) 1, productRequest.getName());
        if (foundProduct.isPresent())
            throw new ResourceNameAlreadyExistsException("Product exists with id: " + foundProduct.get().getId());

        Optional<ChildCategory> foundChildCategory = childCategoryRepository.findChildCategoryByStatusAndId((byte) 1, productRequest.getChildCategoryId());
        if (foundChildCategory.isEmpty())
            throw new ResourceNotFoundException("Child category does not exist with id: " + productRequest.getChildCategoryId());

        Optional<Reputation> foundReputation = reputationRepository.findReputationByStatusAndId((byte) 1, productRequest.getReputationId());
        if (foundReputation.isEmpty())
            throw new ResourceNotFoundException("Reputation does not exist with id: " + productRequest.getReputationId());


        Product newProduct = getProduct(productRequest, foundChildCategory, foundReputation);
        productRepository.save(newProduct);

        foundProduct = productRepository.findProductByStatusAndName((byte) 1, productRequest.getName());
        if (foundProduct.isPresent())
            newProduct = foundProduct.get();

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
        productSkinRepository.saveAll(productSkinList);
        newProduct.setProductSkins(productSkinList);
        // image....
        ProductImageListDto productImageList = getImageFromCookie(request);
        newProduct.setProductImageList(productImageList.getProductImageList());

        for (ProductImage productImage : productImageList.getProductImageList()) {
            productImage.setProduct(newProduct);
            productImageRepository.save(productImage);
        }

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
        newProduct.setChildCategory(foundChildCategory.get());
        newProduct.setReputation(foundReputation.get());
        newProduct.setHowToUse(productRequest.getHowToUse());
        return newProduct;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAllByStatus((byte) 1).stream().map(ProductMapper.INSTANCE::toProductDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsByChildCategoryId(Long childCategoryId) {
        Optional<ChildCategory> childCategory = childCategoryRepository.findChildCategoryByStatusAndId((byte) 1, childCategoryId);
        if (childCategory.isEmpty())
            throw new ResourceNotFoundException("Child category does not exist with id: " + childCategoryId);
        return productRepository.findAllByStatusAndChildCategory((byte) 1, childCategory.get()).stream().map(ProductMapper.INSTANCE::toProductDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto editProduct(ProductRequestDto productRequest, Long productId) {
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
        productSkinRepository.saveAll(productSkinList);
        newProduct.setProductSkins(productSkinList);
        foundProduct.get().setName(productRequest.getName());
        foundProduct.get().setPrice(productRequest.getPrice());
        foundProduct.get().setReputation(foundReputation.get());
        foundProduct.get().setProductSkins(productSkinList);
        foundProduct.get().setIngredients(productRequest.getIngredients());
        foundProduct.get().setSpecification(productRequest.getSpecification());
        foundProduct.get().setStatus((byte) 1);
        foundProduct.get().setMainFunction(productRequest.getMainFunction());
        foundProduct.get().setHowToUse(productRequest.getHowToUse());
//        foundProduct.get().setProductImageList();
        return null;
    }


}
