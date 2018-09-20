package pl.polsl.aei.sklep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.polsl.aei.sklep.repository.OrderRepository;
import pl.polsl.aei.sklep.repository.entity.Order;
import pl.polsl.aei.sklep.repository.entity.ProductOrder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
public class ReportController {

    private OrderRepository orderRepository;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @RequestMapping(value = "/report")
    public ModelAndView showReport() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("selectReport");

        return modelAndView;
    }

    @RequestMapping(value = "/generalreport", method = RequestMethod.POST)
    public ModelAndView showGeneralProfitReport(@RequestParam @DateTimeFormat(pattern = "MM-dd-yyyy") Date dateFrom,
                                                @RequestParam @DateTimeFormat(pattern = "MM-dd-yyyy") Date dateTo) {
        ModelAndView modelAndView = new ModelAndView();
        List<Order> orders = orderRepository.findAllByOrderDateBetween(dateFrom, dateTo);
        Long quantity = orders.stream().flatMap(
                order -> order.getProductOrder().stream()).mapToLong(ProductOrder::getQuantity).sum();
        BigDecimal profit = BigDecimal.ZERO;
        for (Order order : orders) {
            for (ProductOrder productOrder : order.getProductOrder()) {
                profit = profit.add(productOrder.getWarehouse().getSaleCost()
                        .subtract(productOrder.getWarehouse().getSeries().getBuyCost()));
            }
        }
        modelAndView.addObject("quantity", quantity);
        modelAndView.addObject("profit", profit);
        modelAndView.setViewName("report");

        return modelAndView;
    }

    @RequestMapping(value = "/productreport", method = RequestMethod.POST)
    public ModelAndView showProductProfitReport(@RequestParam Long productId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFrom,
                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo) {
        ModelAndView modelAndView = new ModelAndView();
        List<Order> orders = orderRepository.findAllByOrderDateBetween(dateFrom, dateTo);
        Long quantity = orders.stream().flatMap(order -> order.getProductOrder().stream()
                .filter(productOrder -> productOrder.getWarehouse().getProduct().getId().equals(productId)))
                .mapToLong(value -> value.getQuantity()).sum();
        BigDecimal profit = BigDecimal.ZERO;
        for (Order order : orders) {
            for (ProductOrder productOrder : order.getProductOrder()) {
                if (productOrder.getWarehouse().getProduct().getId().equals(productId)) {
                    profit = profit.add(productOrder.getWarehouse().getSaleCost()
                            .subtract(productOrder.getWarehouse().getSeries().getBuyCost()));
                }
            }
        }
        modelAndView.addObject("quantity", quantity);
        modelAndView.addObject("profit", profit);
        modelAndView.setViewName("report");
        return modelAndView;
    }
}
