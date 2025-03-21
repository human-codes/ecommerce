package uz.pdp.salemartpro.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.salemartpro.entity.Address;
import uz.pdp.salemartpro.repo.AddressRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressRepository addressRepository;

    @GetMapping
    public HttpEntity<?> findAll() {
        List<Address> addressList = addressRepository.findAll();
        List<Address> list = addressList.stream().filter(address -> !address.getIsAdmin()).toList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/company")
    public HttpEntity<?> findAdminAddress() {
        List<Address> addressList = addressRepository.findAll();
        Optional<Address> adminAddress = addressList.stream()
                .filter(Address::getIsAdmin)
                .findFirst();

        return new ResponseEntity<>(adminAddress.get(), HttpStatus.OK);
    }

}

