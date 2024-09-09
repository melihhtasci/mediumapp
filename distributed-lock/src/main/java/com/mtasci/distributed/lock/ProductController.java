package com.mtasci.distributed.lock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/product")
public class ProductController {

    private final ProductRepositoryService productRepositoryService;

    public ProductController(ProductRepositoryService productRepositoryService) {
        this.productRepositoryService = productRepositoryService;
    }

    @PostMapping("/make-order/v1")
    public ResponseEntity makeOrder(@RequestBody MakeOrderRequest request) throws Exception {
        final var response = productRepositoryService.makeOrder(request);
        return ResponseEntity.ok(response);
    }

}
