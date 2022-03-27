package juliherms.com.github.ps.service;

import static java.util.logging.Level.FINE;

import com.mongodb.DuplicateKeyException;
import juliherms.com.github.ps.dto.ProductDTO;
import juliherms.com.github.ps.exception.InvalidInputException;
import juliherms.com.github.ps.mapper.ProductMapper;
import juliherms.com.github.ps.model.Product;
import juliherms.com.github.ps.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * This class responsible to implements logical service from Product
 */
@Service
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Autowired
    public ProductService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Method responsible to find product by id
     * @param productId
     * @return
     */
    public Mono<ProductDTO> findProductById(int productId){
        if(productId < 1){
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        return repository.findByProductId(productId)
                .map(e -> mapper.entityToDTO(e));
    }

    /**
     * Thsi classes responsible to save product
     * @param productDTOMono
     * @return
     */
    public Mono<ProductDTO> save(Mono<ProductDTO> productDTOMono){
        return productDTOMono
                .map(e -> mapper.dtoToEntity(e))
                .flatMap(repository::save)
                .log(LOG.getName(), FINE)
                .map(e -> mapper.entityToDTO(e));
    }

    public Mono<Void> delete(int productId){

        if(productId < 1){
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        LOG.debug("deleteProduct: tries to delete an entity with productId: {}", productId);
        return repository.findByProductId(productId)
                .log(LOG.getName(), FINE)
                .map(e -> repository.delete(e))
                .flatMap(e -> e);
    }
}
