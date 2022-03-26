package juliherms.com.github.ps.mapper;

import juliherms.com.github.ps.dto.ProductDTO;
import juliherms.com.github.ps.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

/**
 * Class responsible to convert presentation objects
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Method responsible to convert entity to DTO and ignore 'serviceAddress' attribute
     * @param product
     * @return
     */
    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true)
    })
    ProductDTO entityToDTO(Product product);


    /**
     * Method responsible to convert dto do Entity and ignore attribute 'version' and 'id'
     * @param productDTO
     * @return
     */
    @Mappings({
        @Mapping(target = "id", ignore = true), @Mapping(target = "version", ignore = true)
    })
    Product dtoToEntity(ProductDTO productDTO);


}
