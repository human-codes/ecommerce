package uz.pdp.salemartpro.service;

import org.springframework.stereotype.Service;
import uz.pdp.salemartpro.entity.Delivery;
import uz.pdp.salemartpro.repo.DeliveryRepository;

@Service
public class DelivereyServis {
    private final DeliveryRepository deliveryRepository;

    public DelivereyServis(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public Delivery findById(Integer id) {
        return   deliveryRepository.findById(id).orElse(null);
    }
    public Delivery save(Delivery operator) {
        return deliveryRepository.save(operator);
    }
}
