package uz.pdp.salemartpro.controller;


import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.salemartpro.dto.ProductDto;
import uz.pdp.salemartpro.service.ProductServiceI;

@RequestMapping("/api/product")
@RestController
@Tag(name = "Product API", description = "Endpoints for managing products")
@Server(url = "https://product-server.com", description = "Product Server")
public class ProductController {


    private final ProductServiceI productServiceI;

    public ProductController(ProductServiceI productServiceI) {
        this.productServiceI = productServiceI;
    }


    @GetMapping("/{id}")
    public HttpEntity<?> getProduct(@PathVariable(required = false) int id) {
       return productServiceI.getProducts(id);
    }

    @GetMapping("/each/{id}")
    public HttpEntity<?> getEachProduct(@PathVariable(required = false) int id) {
       return productServiceI.getEachProduct(id);
    }



}
