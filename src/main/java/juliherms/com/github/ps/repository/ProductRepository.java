package juliherms.com.github.ps.repository;

import juliherms.com.github.ps.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * This class responsible to access product collection
 */
@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product,String> {
    Mono<Product> findByProductId(int productId);
}
