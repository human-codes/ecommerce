package uz.pdp.salemartpro.dto;

import lombok.Value;
import uz.pdp.salemartpro.entity.Product;

import java.io.Serializable;

/**
 * DTO for {@link Product}
 */
@Value
public class ProductDto implements Serializable {
    String name;
    Integer price;
    Integer categoryId;
    Integer attachmentId;
}