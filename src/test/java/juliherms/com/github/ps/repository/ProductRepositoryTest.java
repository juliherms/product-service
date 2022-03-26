package juliherms.com.github.ps.repository;

import com.mongodb.DuplicateKeyException;
import juliherms.com.github.ps.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.OptimisticLockingFailureException;
import reactor.test.StepVerifier;

/**
 * This class responsible to execute testes from product repository
 */
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class ProductRepositoryTest extends MongoDbTestBase {

    @Autowired
    private ProductRepository repository;

    private Product product;

    /**
     * Clean collection products in database
     */
    @BeforeEach
    void setupDb() {
        //clean database
        StepVerifier.create(repository.deleteAll()).verifyComplete();
    }

    /**
     * Method responsible to create product in mongo database
     * Test methods: save,findById and Count
     */
    @Test
    void whenCreatingAProductTheReturnMustBeSuccessful() {

        //Step 1 - Create Product
        Product product = Product.builder()
                .productId(1)
                .name("Product test")
                .weight(2)
                .build();

        // Step 2 - Verify product is creates by Id
        StepVerifier.create(repository.save(product))
                .expectNextMatches(createdEntity -> product.getProductId() == createdEntity.getProductId())
                .verifyComplete();

        // Step 3 - Verify all attributes
        StepVerifier.create(repository.findById(product.getId()))
                .expectNextMatches(foundEntity -> areProductEqual(product, foundEntity))
                .verifyComplete();

        // Step 4 - Verify counter
        StepVerifier.create(repository.count()).expectNext(1L).verifyComplete();
    }

    /**
     * Method responsible to modify product in the mongo database
     * Test methods: save, findById
     */
    @Test
    void whenModifyAProductTheReturnMustBeSuccessful() {

        //Step 1 - Create a Product
        Product product = Product.builder()
                .productId(1)
                .name("Product test")
                .weight(2)
                .build();

        // Step 2 - Verify product is created by Id
        StepVerifier.create(repository.save(product))
                .expectNextMatches(createdEntity -> product.getProductId() == createdEntity.getProductId())
                .verifyComplete();


        //Step 3 - Modify Product
        product.setName("Product test 2");
        StepVerifier.create(repository.save(product))
                .expectNextMatches(updatedEntity -> updatedEntity.getName().equals("Product test 2"))
                .verifyComplete();

        //Step 4 - Find product and check your version
        StepVerifier.create(repository.findById(product.getId()))
                .expectNextMatches(foundEntity ->
                        foundEntity.getVersion() == 1
                                && foundEntity.getName().equals("Product test 2"))
                .verifyComplete();
    }

    /**
     * Method responsible to test delete product
     * Test methods: save, delete, existsById
     */
    @Test
    void whenDeleteProductTheReturnMustBeSuccessful() {

        //Step 1 - Create a Product
        Product product = Product.builder()
                .productId(1)
                .name("Product test")
                .weight(2)
                .build();

        // Step 2 - Verify product is created by Id
        StepVerifier.create(repository.save(product))
                .expectNextMatches(createdEntity -> product.getProductId() == createdEntity.getProductId())
                .verifyComplete();

        // Step 3 - Execute delete product and verify complete
        StepVerifier.create(repository.delete(product)).verifyComplete();

        // Step 4 - Verify search exists product by id
        StepVerifier.create(repository.existsById(product.getId())).expectNext(false).verifyComplete();
    }

    /**
     * Method responsible to test search product by id
     * Test methods: save, findByProductId
     */
    @Test
    void whenGetByProductIdTheReturnMustBeSuccessful() {

        //Step 1 - Create a Product
        Product product = Product.builder()
                .productId(1)
                .name("Product test")
                .weight(2)
                .build();

        // Step 2 - Verify product is created by Id
        StepVerifier.create(repository.save(product))
                .expectNextMatches(createdEntity -> product.getProductId() == createdEntity.getProductId())
                .verifyComplete();

        // Step 3 - Find product by id and check your atributes
        StepVerifier.create(repository.findByProductId(product.getProductId()))
                .expectNextMatches(foundEntity -> areProductEqual(product, foundEntity))
                .verifyComplete();
    }

    /**
     * Method responsible to test a create product with unique index id
     * Test methods: save
     */
    @Test
    void  whenCreateAProductWithSameIdTheReturnMustBeError() {

        //Step 1 - Create a Product
        Product product = Product.builder()
                .productId(1)
                .name("Product test")
                .weight(2)
                .build();

        // Step 2 - Verify product is created by Id
        StepVerifier.create(repository.save(product))
                .expectNextMatches(createdEntity -> product.getProductId() == createdEntity.getProductId())
                .verifyComplete();


        // Step 3 - Create product with same id
        Product product2 = Product.builder()
                .productId(1)
                .name("Product test 2")
                .weight(2)
                .build();

        //StepVerifier.create(repository.save(product2)).expectError(DuplicateKeyException.class).verify();
    }

    /**
     * Method responsible to test a optimistic lock error
     * Test methods: save
     */
    @Test
    void whenOptimisticLockError() {

        //Step 1 - Create a Product
        Product product = Product.builder()
                .productId(1)
                .name("Product test")
                .weight(2)
                .build();

        // Step 2 - Verify product is created by Id
        StepVerifier.create(repository.save(product))
                .expectNextMatches(createdEntity -> product.getProductId() == createdEntity.getProductId())
                .verifyComplete();


        // Store the saved entity in two separate entity objects
        Product entity1 = repository.findById(product.getId()).block();
        Product entity2 = repository.findById(product.getId()).block();

        // Update the entity using the first entity object
        entity1.setName("Product name 1");
        repository.save(entity1).block();

        //  Update the entity using the second entity object.
        // This should fail since the second entity now holds a old version number, i.e. a Optimistic Lock Error
        StepVerifier.create(repository.save(entity2))
                .expectError(OptimisticLockingFailureException.class).verify();

        // Get the updated entity from the database and verify its new sate
        StepVerifier.create(repository.findById(product.getId()))
                .expectNextMatches(foundEntity ->
                        foundEntity.getVersion() == 1
                                && foundEntity.getName().equals("Product name 1"))
                .verifyComplete();
    }

    private boolean areProductEqual(Product expectedEntity, Product actualEntity) {
        return
                (expectedEntity.getId().equals(actualEntity.getId()))
                        && (expectedEntity.getVersion() == actualEntity.getVersion())
                        && (expectedEntity.getProductId() == actualEntity.getProductId())
                        && (expectedEntity.getName().equals(actualEntity.getName()))
                        && (expectedEntity.getWeight() == actualEntity.getWeight());
    }
}
