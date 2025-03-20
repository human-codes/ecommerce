package uz.pdp.salemartpro.service;

import org.springframework.http.HttpEntity;
import uz.pdp.salemartpro.dto.CategoryDto;

public interface CategoryServiceI {
    HttpEntity<?> getCategories();


}
