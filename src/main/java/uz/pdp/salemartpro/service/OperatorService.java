package uz.pdp.salemartpro.service;


import org.springframework.stereotype.Service;
import uz.pdp.salemartpro.entity.Operator;
import uz.pdp.salemartpro.repo.OperatorRepository;

@Service
public class OperatorService {


    private final OperatorRepository operatorRepository;

    public OperatorService(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }
    public Operator findById(Integer id) {
        return operatorRepository.findById(id).orElse(null);
    }
    public Operator save(Operator operator) {
        return operatorRepository.save(operator);
    }
}
