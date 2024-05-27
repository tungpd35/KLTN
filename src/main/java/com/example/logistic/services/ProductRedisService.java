package com.example.logistic.services;

import com.example.logistic.entities.Product;
import com.example.logistic.models.ProductPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductRedisService implements IProductRedisService{
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;
    private String getKeyFrom(Long id, String keyword, PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        String key = String.format("all_product:%d:%d:%d", id, pageNumber, pageSize);
        return key;
    }
    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Override
    public ProductPage getAllProducts(Long id, String keyword, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(id, keyword, pageRequest);
        String json = (String) redisTemplate.opsForValue().get(key);
        ProductPage products = json != null ?
                redisObjectMapper.readValue(json, new TypeReference<ProductPage>() {})
                : null;
        return products;
    }

    @Override
    public void saveAllProducts(ProductPage products, Long id, String keyword, PageRequest pageRequest) throws JsonProcessingException {
        String key = getKeyFrom(id, keyword, pageRequest);
        String json = redisObjectMapper.writeValueAsString(products);
        redisTemplate.opsForValue().set(key, json);
    }
}
