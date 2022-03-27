package juliherms.com.github.ps.controller;

import juliherms.com.github.ps.dto.ProductDTO;
import juliherms.com.github.ps.model.Product;
import juliherms.com.github.ps.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * This class responsible to expose all endpoints from product
 */
@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private static final Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    @PostMapping
    public Mono<ProductDTO> create(@RequestBody Mono<ProductDTO> productDTO)
    {
        return service.save(productDTO);
    }

    @GetMapping("{productId}")
    public Mono<ResponseEntity<ProductDTO>> getProductById(@PathVariable Integer productId){
        return service.findProductById(productId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{productId}")
    public Mono<Void> delete(@PathVariable Integer productId){
        return service.delete(productId);
    }
}
