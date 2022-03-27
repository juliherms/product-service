package juliherms.com.github.ps.service;

import static org.mockito.Mockito.when;

import juliherms.com.github.ps.dto.ProductDTO;
import juliherms.com.github.ps.exception.InvalidInputException;
import juliherms.com.github.ps.model.Product;
import org.springframework.boot.test.mock.mockito.MockBean;
import juliherms.com.github.ps.repository.MongoDbTestBase;
import juliherms.com.github.ps.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
public class ProductServiceTest {

    private static final int PRODUCT_ID_NOT_FOUND = 2;

    private static final Product product =  Product.builder()
            .productId(1)
            .weight(1)
            .name("Product test with duplicate key").build();

    @MockBean
    private ProductRepository repository;

    @Autowired
    private ProductService service;

    @BeforeEach
    void setupDb() {

        System.out.println(product.toString());

        when(repository.save(product))
                .thenThrow(new InvalidInputException("Duplicate key, Product Id: " + product.getProductId()));
    }

    @Test
    void getProductById() {

        ProductDTO productDTO = ProductDTO.builder()
                .productId(1)
                .name("Product test with duplicate key")
                .weight(1).build();

       // StepVerifier.create(service.save(productDTO)).expectError(InvalidInputException.class).verify();
    }
}
