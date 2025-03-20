package uz.pdp.salemartpro.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.salemartpro.dto.ProductDto;
import uz.pdp.salemartpro.entity.Attachment;
import uz.pdp.salemartpro.entity.AttachmentContent;
import uz.pdp.salemartpro.entity.Product;
import uz.pdp.salemartpro.repo.AttachmentContentRepository;
import uz.pdp.salemartpro.repo.AttachmentRepository;
import uz.pdp.salemartpro.repo.CategoryRepository;
import uz.pdp.salemartpro.repo.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements ProductServiceI{

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, AttachmentRepository attachmentRepository, AttachmentContentRepository attachmentContentRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public HttpEntity<?> getProducts(int id) {
        if (id==0){
            List<Product> products = productRepository.findAll();
            if (products.isEmpty()) {
                return ResponseEntity.status(204).build(); // No content
            }
           return ResponseEntity.ok().body(products);
        }
        else {
            List<Product> productsbyCategoryId = productRepository.findByCategoryId(id);
            if (productsbyCategoryId.isEmpty()) {
                return ResponseEntity.status(204).build(); // No content
            }
            return ResponseEntity.ok(productRepository.findByCategoryId(id));
        }
    }

    @Override
    public HttpEntity<?> getEachProduct(int id) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isPresent()){
            return ResponseEntity.ok(byId.get());
        }
        else return ResponseEntity.status(404).build();
    }


}
