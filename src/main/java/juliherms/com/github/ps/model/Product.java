package juliherms.com.github.ps.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This class responsible to represent products
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "products")
@Builder
public class Product {

    @Id
    private String id;

    @Version
    private Integer version;

    //The @Indexed(unique = true) annotation is used to get
    // a unique index created for the business key, productId.
    @Indexed(unique = true)
    private int productId;

    private String name;
    private int weight;
}
