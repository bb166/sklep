package pl.polsl.aei.sklep.service;

import pl.polsl.aei.sklep.dto.ReportDTO;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Transactional
public interface ReportService {
    ReportDTO getProductProfitReport(Long productId, Date dateFrom, Date dateTo);

    List<ReportDTO> getGeneralProfitReport(Date dateFrom, Date dateTo);

    BigDecimal getTotalProfit(Date dateFrom, Date dateTo);
}
