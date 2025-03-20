package uz.pdp.salemartpro.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.salemartpro.dto.CategoryDto;
import uz.pdp.salemartpro.dto.ProductDto;
import uz.pdp.salemartpro.dto.RegisterDto;
import uz.pdp.salemartpro.service.AdminServiceI;

@RestController
@RequestMapping("/api/admin")
@MultipartConfig

public class AdminController {
    private final AdminServiceI adminServiceI;

    public AdminController(AdminServiceI adminServiceI) {
        this.adminServiceI = adminServiceI;
    }

    @DeleteMapping("/category/{id}")
    public HttpEntity<?> deleteCategory(@PathVariable int id) {
        return adminServiceI.deleteCategory(id);
    }

    @PostMapping("/category")
    public HttpEntity<?> addCategory(@RequestBody CategoryDto categoryDto) {
        return adminServiceI.addCategory(categoryDto);
    }

    @PutMapping("/category/{id}")
    public HttpEntity<?> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable int id) {
        return adminServiceI.updateCategory(categoryDto, id);
    }

    @PostMapping("/product")
    public HttpEntity<?> addProduct(@RequestBody ProductDto productDTO) {
        return adminServiceI.addProduct(productDTO);
    }

    @PutMapping("/product/{productId}")
    public HttpEntity<?> updateProduct(@PathVariable Integer productId,@RequestBody ProductDto productDTO) {
        return adminServiceI.updateProduct(productDTO,productId);
    }

    @DeleteMapping("/product/{id}")
    public HttpEntity<?> deleteProduct(@PathVariable int id) {
        return adminServiceI.deleteProduct(id);
    }

    @GetMapping("/deliverers")
    public HttpEntity<?> getDeliverers() {
        return adminServiceI.getDeliverers();
    }

    @GetMapping("/operators")
    public HttpEntity<?> getOperators() {
        return adminServiceI.getOperators();
    }

    @PostMapping("create-employee")
    public HttpEntity<?> createDelivererVsOperator(@RequestBody RegisterDto registerDto) {
        return adminServiceI.createDelivererVsOperator(registerDto);
    }

    @PutMapping("edit-employee{userId}")
    public HttpEntity<?> editDelivererVsOperator(@RequestBody RegisterDto registerDto, @PathVariable Integer userId) {
        return adminServiceI.editDelivererVsOperator(userId, registerDto);
    }

    @GetMapping("/deliverer{id}")
    public HttpEntity<?> getDeliverer(@PathVariable int id) {
        return adminServiceI.getOperator(id);
    }

    @GetMapping("/operator{id}")
    public HttpEntity<?> getOperator(@PathVariable int id) {
        return adminServiceI.getDeliverer(id);
    }


}
