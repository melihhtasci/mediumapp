package com.mtasci.distributed.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
public class ProductRepositoryService {

    private final LockRegistry lockRegistry;
    private final ProductRepository productRepository;
    private final RedissonClient redissonClient;

    public ProductRepositoryService(LockRegistry lockRegistry,
                                    ProductRepository productRepository,
                                    RedissonClient redissonClient) {
        this.lockRegistry = lockRegistry;
        this.productRepository = productRepository;
        this.redissonClient = redissonClient;
    }



    private void updateStock(MakeOrderRequest request, Product product) {
        int newStock = product.getStock() - request.getStock();
        product.setStock(newStock);
        product.setUpdatedBy(request.getUpdatedBy());
        productRepository.save(product);
    }

    Product checkStock(MakeOrderRequest request) throws Exception {
        var optionalProduct = productRepository.findById(request.getId());
        if (optionalProduct.isEmpty()) {
            System.out.println("Product not found");
            throw new Exception("Product not found");
        }

        final var product = optionalProduct.get();
        System.out.println(product.getStock() + "-" + request.getStock());
        if (product.getStock() < request.getStock()) {
            System.out.println(product.getStock() + " out of stock");
            throw new Exception(product.getName() + " out of stock");
        }
        return product;
    }

    public String makeOrder(MakeOrderRequest request) throws Exception {
        String key = "ID-" + request.getId().toString();
        RLock lock = redissonClient.getLock(key);

        boolean lockAcquired = lock.tryLock(5, TimeUnit.SECONDS);
        if (lockAcquired) {
            try {
                final var product = checkStock(request);
                updateStock(request, product);

                Thread.sleep(request.getDelay());

                return new StringBuffer().append("ORDER COMPLETED").append("\n\r")
                        .append(product.getName()).append(" stock count is : ").append(product.getStock()).toString();
            } finally {
                if (lockAcquired) {
                    lock.unlock();
                }
            }
        } else {
            System.out.println("The product you want to buy is already locked");
        }
        return checkStock(request).toString();
    }


    public String order(MakeOrderRequest request) throws Exception {

        String key = "ID-" + request.getId().toString();
        Lock lock = lockRegistry.obtain(key);
        boolean lockAcquired = lock.tryLock(5, TimeUnit.SECONDS);

        if (lockAcquired) {
            try {
                final var product = checkStock(request);
                updateStock(request, product);
                Thread.sleep(request.getDelay());

                return new StringBuffer().append("ORDER COMPLETED").append("\n\r")
                        .append(product.getName()).append(" stock count is : ").append(product.getStock()).toString();
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("The product you want to buy is already locked");
        }
        checkStock(request);
        return "OK";
    }

}
