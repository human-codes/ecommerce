package uz.pdp.salemartpro.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import uz.pdp.salemartpro.dto.CategoryDto;
import uz.pdp.salemartpro.dto.ProductDto;
import uz.pdp.salemartpro.dto.RegisterDto;

public interface AdminServiceI {
    HttpEntity<?> deleteCategory(int id);

    HttpEntity<?> addCategory(CategoryDto categoryDto);

    HttpEntity<?> updateCategory(CategoryDto categoryDto, int id);

    HttpEntity<?> addProduct(ProductDto productDTO);

    HttpEntity<?> updateProduct(ProductDto productDTO, Integer productId);

    HttpEntity<?> deleteProduct(int id);

    HttpEntity<?> getDeliverers();

    HttpEntity<?> getOperators();

    HttpEntity<?> createDelivererVsOperator(RegisterDto registerDto);

    ResponseEntity<?> editDelivererVsOperator(Integer userId, RegisterDto registerDto);


    HttpEntity<?> getOperator(int id);

    HttpEntity<?> getDeliverer(int id);
}
