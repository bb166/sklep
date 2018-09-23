package pl.polsl.aei.sklep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.polsl.aei.sklep.repository.OrderRepository;
import pl.polsl.aei.sklep.service.ReportService;

import java.util.Date;

@Controller
public class ReportController {

    private OrderRepository orderRepository;
    private ReportService reportService;


    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }

    @RequestMapping(value = "/selectReport")
    public ModelAndView selectReport() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("selectReport");

        return modelAndView;
    }

    @RequestMapping(value = "/report")
    public ModelAndView showSelectGeneralProfitReport(@RequestParam String type) {
        ModelAndView modelAndView = new ModelAndView();
        if (type.equals("general")) {
            modelAndView.setViewName("selectGeneralReport");
        }
        if (type.equals("product")) {
            modelAndView.setViewName("selectProductReport");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/generalreport", method = RequestMethod.POST)
    public ModelAndView showGeneralProfitReport(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFrom,
                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", reportService.getGeneralProfitReport(dateFrom, dateTo));
        modelAndView.addObject("total", reportService.getTotalProfit(dateFrom, dateTo).toString());
        modelAndView.setViewName("generalReport");

        return modelAndView;
    }

    @RequestMapping(value = "/productreport", method = RequestMethod.POST)
    public ModelAndView showProductProfitReport(@RequestParam Long productId,
                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFrom,
                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("product", reportService.getProductProfitReport(productId, dateFrom, dateTo));
        modelAndView.setViewName("productReport");
        return modelAndView;
    }
}
