package uz.pdp.salemartpro.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.salemartpro.dto.CategoryDto;
import uz.pdp.salemartpro.dto.ProductDto;
import uz.pdp.salemartpro.dto.RegisterDto;
import uz.pdp.salemartpro.entity.*;
import uz.pdp.salemartpro.repo.*;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService implements AdminServiceI{
    private final CategoryRepository categoryRepository;
    private final AttachmentRepository attachmentRepository;
    private final ProductRepository productRepository;
    private final AttachmentContentRepository attachmentContentRepository;
    private final DeliveryRepository deliveryRepository;
    private final OperatorRepository operatorRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AdminService(CategoryRepository categoryRepository, AttachmentRepository attachmentRepository, ProductRepository productRepository, AttachmentContentRepository attachmentContentRepository,
                        DeliveryRepository deliveryRepository, OperatorRepository operatorRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.categoryRepository = categoryRepository;
        this.attachmentRepository = attachmentRepository;
        this.productRepository = productRepository;
        this.attachmentContentRepository = attachmentContentRepository;
        this.deliveryRepository = deliveryRepository;
        this.operatorRepository = operatorRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public HttpEntity<?> deleteCategory(int id) {
        if (categoryRepository.existsById(id)){
            categoryRepository.deleteById(id);
            return ResponseEntity.status(204).build();
        }
        else return ResponseEntity.status(404).build();
    }

    @Override
    public HttpEntity<?> addCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        try {
            category = categoryRepository.save(category);
            return ResponseEntity.status(201).body(category);
        } catch (Exception e) {
            // Handle any potential error (e.g., database issues)
            return ResponseEntity.status(500).body("Error creating category: " + e.getMessage());
        }
    }

    @Override
    public HttpEntity<?> updateCategory(CategoryDto categoryDto, int id) {
        Optional<Category> byId = categoryRepository.findById(id);
        if (byId.isPresent()){
            Category category = byId.get();
            category.setName(categoryDto.getName());
            categoryRepository.save(category);
            return ResponseEntity.status(204).build();
        }
        else return ResponseEntity.status(404).build();
    }

    @Override
    public HttpEntity<?> addProduct(ProductDto productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setCategory(categoryRepository.findById(productDTO.getCategoryId()).orElseThrow());
        product.setAttachment(attachmentRepository.findById(productDTO.getAttachmentId()).orElseThrow());
        productRepository.save(product);
        return ResponseEntity.status(201).build();
    }

    @Override
    public HttpEntity<?> updateProduct(ProductDto productDTO, Integer productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setCategory(categoryRepository.findById(productDTO.getCategoryId()).orElseThrow());
        product.setAttachment(attachmentRepository.findById(productDTO.getAttachmentId()).orElseThrow());
        productRepository.save(product);
        return ResponseEntity.status(204).build();
    }

    @Override
    public HttpEntity<?> deleteProduct(int id) {
        Product product = productRepository.findById(id).orElseThrow();
        Attachment attachment = product.getAttachment();
        AttachmentContent byAttachmentId = attachmentContentRepository.findByAttachmentId(attachment.getId());
        attachmentContentRepository.delete(byAttachmentId);
        productRepository.deleteById(id);
        attachmentRepository.delete(attachment);
        return ResponseEntity.status(204).build();
    }

    @Override
    public HttpEntity<?> getDeliverers() {
        List<Delivery> all = deliveryRepository.findAll();
        if (all.isEmpty()) return ResponseEntity.status(404).build();
        return ResponseEntity.status(200).body(all);
    }

    @Override
    public HttpEntity<?> getOperators() {
        List<Operator> all = operatorRepository.findAll();
        if (all.isEmpty()) return ResponseEntity.status(404).build();
        return ResponseEntity.status(200).body(all);
    }

    @Override
    public HttpEntity<?> createDelivererVsOperator(RegisterDto registerDto) {
        List<Role> roles = registerDto.getRoles().stream()
                .map(roleName -> roleRepository.findByRoleName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .toList();
        if (registerDto.getVehicleNumber()==null){
            Operator operator = new Operator();
            operator.setUsername(registerDto.getUsername());
            operator.setEmail(registerDto.getEmail());
            operator.setPhone(registerDto.getPhone());
            operator.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            operator.setRoles(roles);
            operatorRepository.save(operator);
        }
        else {
            Delivery delivery = new Delivery();
            delivery.setUsername(registerDto.getUsername());
            delivery.setEmail(registerDto.getEmail());
            delivery.setPhone(registerDto.getPhone());
            delivery.setRoles(roles);
            delivery.setVehicleNumber(registerDto.getVehicleNumber());
            delivery.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            deliveryRepository.save(delivery);
        }
        return ResponseEntity.status(201).build();
    }



    @Override
    public ResponseEntity<?> editDelivererVsOperator(Integer userId, RegisterDto registerDto) {
        if (registerDto.getVehicleNumber() == null) {
            // Update Operator
            Optional<Operator> optionalOperator = operatorRepository.findById(userId);
            if (optionalOperator.isPresent()) {
                Operator operator = optionalOperator.get();
                updateOperatorFields(operator, registerDto);
                operatorRepository.save(operator);
                return ResponseEntity.ok("Operator updated successfully.");
            }
        } else {
            // Update Delivery
            Optional<Delivery> optionalDelivery = deliveryRepository.findById(userId);
            if (optionalDelivery.isPresent()) {
                Delivery delivery = optionalDelivery.get();
                updateDeliveryFields(delivery, registerDto);
                deliveryRepository.save(delivery);
                return ResponseEntity.ok("Delivery updated successfully.");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    @Override
    public HttpEntity<?> getOperator(int id) {
        Operator operator = operatorRepository.findById(id).orElseThrow();
        return ResponseEntity.status(200).body(operator);
    }

    @Override
    public HttpEntity<?> getDeliverer(int id) {
        Delivery delivery = deliveryRepository.findById(id).orElseThrow();
        return ResponseEntity.status(200).body(delivery);
    }

    private void updateOperatorFields(Operator operator, RegisterDto registerDto) {
        List<Role> roles = registerDto.getRoles().stream()
                .map(roleName -> roleRepository.findByRoleName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .toList();

        if (registerDto.getRoles() != null) operator.setRoles(roles);
        if (registerDto.getUsername() != null) operator.setUsername(registerDto.getUsername());
        if (registerDto.getEmail() != null) operator.setEmail(registerDto.getEmail());
        if (registerDto.getPhone() != null) operator.setPhone(registerDto.getPhone());
        if (registerDto.getPassword() != null) operator.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        if (registerDto.getRoles() != null && !registerDto.getRoles().isEmpty()) operator.setRoles(roles);
    }

    private void updateDeliveryFields(Delivery delivery, RegisterDto registerDto) {
        List<Role> roles = registerDto.getRoles().stream()
                .map(roleName -> roleRepository.findByRoleName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .toList();

        if(registerDto.getRoles() != null) delivery.setRoles(roles);
        if (registerDto.getUsername() != null) delivery.setUsername(registerDto.getUsername());
        if (registerDto.getEmail() != null) delivery.setEmail(registerDto.getEmail());
        if (registerDto.getPhone() != null) delivery.setPhone(registerDto.getPhone());
        if (registerDto.getPassword() != null) delivery.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        if (registerDto.getRoles() != null && !registerDto.getRoles().isEmpty()) delivery.setRoles(roles);
        if (registerDto.getVehicleNumber() != null) delivery.setVehicleNumber(registerDto.getVehicleNumber());
    }



}
