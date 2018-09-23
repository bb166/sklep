package pl.polsl.aei.sklep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.aei.sklep.dto.ReportDTO;
import pl.polsl.aei.sklep.repository.OrderRepository;
import pl.polsl.aei.sklep.repository.ProductRepository;
import pl.polsl.aei.sklep.repository.entity.Order;
import pl.polsl.aei.sklep.repository.entity.ProductOrder;
import pl.polsl.aei.sklep.repository.entity.Warehouse;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
//        Optional<BigDecimal> saleCost = orders.stream().flatMap(order -> order.getProductOrder().stream().map(ProductOrder::getWarehouse))
//                .map(Warehouse::getSaleCost).findAny();
//        List<Warehouse> warehouseList = orders.stream().flatMap(order -> order.getProductOrder().stream().map(ProductOrder::getWarehouse))
//                .collect(Collectors.toList());
//        Long soldCount = orders.stream().flatMap(order -> order.getProductOrder().stream()).mapToLong(ProductOrder::getQuantity).sum();
//        return calculateProfit(orders, soldCount, saleCost, warehouseList);

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
            reportDTO.setProfit(getProfitForProduct(orders, productId, soldCount).toString());
        });
        return reportDTO;
    }

    private BigDecimal getProfitForProduct(List<Order> orders, Long productId, Long soldCount) {
        Optional<BigDecimal> saleCost = orders.stream().flatMap(order -> order.getProductOrder().stream().map(ProductOrder::getWarehouse))
                .filter(warehouse -> warehouse.getProduct().getId().equals(productId)).map(Warehouse::getSaleCost).findAny();
        List<Warehouse> warehouseList = orders.stream().flatMap(order -> order.getProductOrder().stream().map(ProductOrder::getWarehouse))
                .filter(warehouse -> warehouse.getProduct().getId().equals(productId))
                .collect(Collectors.toList());

        BigDecimal profit = BigDecimal.ZERO;
        for (Order order : orders) {
            for (ProductOrder productOrder : order.getProductOrder()) {
                if (productOrder.getWarehouse().getProduct().getId().equals(productId)) {
                    profit = profit.add(productOrder.getWarehouse().getSaleCost()
                            .subtract(productOrder.getWarehouse().getSeries().getBuyCost()));
                }
            }
        }
        return profit;//calculateProfit(orders, soldCount, saleCost, warehouseList);
    }

    private BigDecimal calculateProfit(List<Order> orders, Long soldCount, Optional<BigDecimal> saleCost, List<Warehouse> warehouseList) {
        BigDecimal buyCost = BigDecimal.ZERO;
        for (Warehouse warehouse : warehouseList) {
            Long quantity = orders.stream().flatMap(order -> order.getProductOrder().stream())
                    .filter(productOrder -> productOrder.getWarehouse().equals(warehouse))
                    .mapToLong(ProductOrder::getQuantity).sum();
            buyCost = buyCost.add(warehouse.getSeries().getBuyCost().multiply(BigDecimal.valueOf(quantity)));
        }
        BigDecimal profit = BigDecimal.ZERO;
        if (saleCost.isPresent()) {
            profit = profit.add(saleCost.get()).multiply(BigDecimal.valueOf(soldCount)).subtract(buyCost);
        }
        return profit;
    }
}
