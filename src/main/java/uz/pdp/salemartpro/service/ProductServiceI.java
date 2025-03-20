package uz.pdp.salemartpro.service;

import org.springframework.http.HttpEntity;
import uz.pdp.salemartpro.dto.ProductDto;

public interface ProductServiceI {

    HttpEntity<?> getProducts(int id);

    HttpEntity<?> getEachProduct(int id);


}
