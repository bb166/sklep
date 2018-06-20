package pl.polsl.aei.sklep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.polsl.aei.sklep.service.ProductService;

@Controller
public class CategoriesController {

    private ProductService productService;

    @Autowired
    public CategoriesController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping("/categories")
    public ModelAndView showHeadItems(@RequestParam String category) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("productList");
        modelAndView.addObject("category", category);
        modelAndView.addObject("products",
                productService.getAllProductsFromCategory(ProductService.CategoryName.findByDenormalizeName(category)));

        return modelAndView;
    }
}