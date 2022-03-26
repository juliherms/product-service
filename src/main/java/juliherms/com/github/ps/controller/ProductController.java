package juliherms.com.github.ps.controller;

import juliherms.com.github.ps.dto.ProductDTO;
import juliherms.com.github.ps.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * This class responsible to expose all endpoints from product
 */
@RestController
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping
    public Mono<ProductDTO> create(ProductDTO productDTO){
        return service.save(productDTO);
    }

    @DeleteMapping
    public Mono<Void> delete(int productId){
        return service.delete(productId);
    }
}
