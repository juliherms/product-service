package juliherms.com.github.ps.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDTO {
    private int productId;
    private String name;
    private int weight;
    private String serviceAddress;
}
