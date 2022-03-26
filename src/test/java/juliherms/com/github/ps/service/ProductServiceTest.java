package juliherms.com.github.ps.service;

import juliherms.com.github.ps.repository.MongoDbTestBase;
import juliherms.com.github.ps.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceTest extends MongoDbTestBase {

    @Autowired
    private ProductRepository repository;

    private ProductService service;

    @BeforeEach
    void setupDb() {
        repository.deleteAll().block();
    }

    @Test
    void getProductById() {

        int productId = 1;

    }
}
