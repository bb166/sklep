package pl.polsl.aei.sklep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.aei.sklep.dto.ReportDTO;
import pl.polsl.aei.sklep.repository.OrderRepository;
import pl.polsl.aei.sklep.repository.ProductRepository;
import pl.polsl.aei.sklep.repository.entity.Order;
import pl.polsl.aei.sklep.repository.entity.ProductOrder;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ReportDTO> getGeneralProfitReport(Date dateFrom, Date dateTo) {
        return productRepository.findAll().stream().map(product -> getProductProfitReport(product.getId(), dateFrom, dateTo))
                .sorted(Comparator.comparing(ReportDTO::getProductId)).collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalProfit(Date dateFrom, Date dateTo) {
        List<Order> orders = orderRepository.findAllByOrderDateBetween(dateFrom, dateTo);
        BigDecimal profit = BigDecimal.ZERO;
        for (Order order : orders) {
            for (ProductOrder productOrder : order.getProductOrder()) {
                profit = profit.add(productOrder.getWarehouse().getSaleCost()
                        .subtract(productOrder.getWarehouse().getSeries().getBuyCost()));
            }
        }
        return profit;
    }

    @Override
    public ReportDTO getProductProfitReport(Long productId, Date dateFrom, Date dateTo) {
        ReportDTO reportDTO = new ReportDTO();
        productRepository.findById(productId).ifPresent(product -> {
            reportDTO.setProductId(productId.toString());
            reportDTO.setProductName(product.getName());
            List<Order> orders = orderRepository.findAllByOrderDateBetween(dateFrom, dateTo);
            Long soldCount = orders.stream().flatMap(
                    order -> order.getProductOrder().stream().filter(productOrder -> productOrder.getWarehouse().getProduct().getId().equals(productId))
            )
                    .mapToLong(ProductOrder::getQuantity).sum();
            reportDTO.setSoldCount(soldCount.toString());
            reportDTO.setProfit(getProfitForProduct(orders, productId).toString());
        });
        return reportDTO;
    }

    private BigDecimal getProfitForProduct(List<Order> orders, Long productId) {
        BigDecimal profit = BigDecimal.ZERO;
        for (Order order : orders) {
            for (ProductOrder productOrder : order.getProductOrder()) {
                if (productOrder.getWarehouse().getProduct().getId().equals(productId)) {
                    profit = profit.add(productOrder.getWarehouse().getSaleCost()
                            .subtract(productOrder.getWarehouse().getSeries().getBuyCost()));
                }
            }
        }
        return profit;
    }
}