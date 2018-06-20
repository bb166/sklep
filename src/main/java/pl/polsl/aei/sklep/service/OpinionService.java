package pl.polsl.aei.sklep.service;

import pl.polsl.aei.sklep.dto.OpinionDTO;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface OpinionService {
    List<OpinionDTO> getOpinionsForProduct(Long productId);
    void addOpinionToProduct(OpinionDTO opinionDTO);
}
