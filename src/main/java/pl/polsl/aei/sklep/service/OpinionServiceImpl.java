package pl.polsl.aei.sklep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.aei.sklep.dto.OpinionDTO;
import pl.polsl.aei.sklep.repository.ProductRepository;
import pl.polsl.aei.sklep.repository.RateRepository;
import pl.polsl.aei.sklep.repository.UserRepository;
import pl.polsl.aei.sklep.repository.entity.Rate;
import pl.polsl.aei.sklep.tool.Tuple;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpinionServiceImpl implements OpinionService {

    private ProductRepository productRepository;
    private UserRepository userRepository;
    private RateRepository rateRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRateRepository(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    @Override
    public List<OpinionDTO> getOpinionsForProduct(Long productId) {
        return productRepository.findById(productId).map(product -> product
                        .getRates()
                        .stream()
                        .map(this::mapRateToOpinionDTO)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public void addOpinionToProduct(OpinionDTO opinionDTO) {
        productRepository
                .findById(Long.parseLong(opinionDTO.getProductId()))
                .map(product -> new Tuple<>(userRepository.findUserByUsername(opinionDTO.getUser()), product))
                .ifPresent(tuple -> {

                    Rate rate = new Rate();
                    rate.setComment(opinionDTO.getComment());
                    rate.setValue(Double.parseDouble(opinionDTO.getValue()));
                    rate.setProduct(tuple._2);
                    rate.setUser(tuple._1);
                    rateRepository.save(rate);

                });
    }

    private OpinionDTO mapRateToOpinionDTO(Rate rate) {
        OpinionDTO opinionDTO = new OpinionDTO();

        opinionDTO.setComment(rate.getComment());
        opinionDTO.setValue(String.format("%.2f",rate.getValue()));
        opinionDTO.setUser(rate.getUser().getUsername());

        return opinionDTO;
    }
}
