package uz.pdp.salemartpro.dto;

import lombok.Value;
import uz.pdp.salemartpro.entity.Category;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
@Value
public class CategoryDto implements Serializable {
    String name;
}