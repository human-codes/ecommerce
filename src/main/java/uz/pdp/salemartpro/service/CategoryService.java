package uz.pdp.salemartpro.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.salemartpro.dto.CategoryDto;
import uz.pdp.salemartpro.entity.Category;
import uz.pdp.salemartpro.repo.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements CategoryServiceI{

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public HttpEntity<?> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        else{
            return ResponseEntity.ok(categories);
        }
    }


}
