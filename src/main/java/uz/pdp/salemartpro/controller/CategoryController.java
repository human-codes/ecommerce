package uz.pdp.salemartpro.controller;

import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import uz.pdp.salemartpro.service.CategoryServiceI;



@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category API", description = "Endpoints for managing categories")
@Server(url = "https://category-server.com", description = "Category Server")
public class CategoryController {

    private final CategoryServiceI categoryServiceI;

    public CategoryController(CategoryServiceI categoryServiceI) {
        this.categoryServiceI = categoryServiceI;
    }

    @GetMapping
    public HttpEntity<?> getCategories() {
        return categoryServiceI.getCategories();
    }


}

