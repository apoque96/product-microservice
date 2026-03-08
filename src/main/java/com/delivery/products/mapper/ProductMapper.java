package com.delivery.products.mapper;

import com.delivery.products.dto.ProductCreateRequest;
import com.delivery.products.dto.ProductResponse;
import com.delivery.products.dto.ProductUpdateRequest;
import com.delivery.products.entity.Product;

public class ProductMapper {
    public static ProductResponse mapProductToProductResponse(Product product, ProductResponse productResponse){
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setPrice(product.getPrice());
        productResponse.setDescription(product.getDescription());
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }

    public static Product mapProductCreateRequestToProduct(ProductCreateRequest productCreateRequest, Product product){
        product.setName(productCreateRequest.getName());
        product.setPrice(productCreateRequest.getPrice());
        product.setDescription(productCreateRequest.getDescription());
        return product;
    }

    public static Product mapProductUpdateRequestToProduct(ProductUpdateRequest productUpdateRequest, Product product){
        product.setName(productUpdateRequest.getName());
        product.setPrice(productUpdateRequest.getPrice());
        product.setDescription(productUpdateRequest.getDescription());
        return product;
    }
}
