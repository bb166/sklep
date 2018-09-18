package pl.polsl.aei.sklep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.polsl.aei.sklep.service.BasketService;

import java.security.Principal;

@Controller
public class BasketController {

    private BasketService basketService;

    @Autowired
    public void setBasketService(BasketService basketService) {
        this.basketService = basketService;
    }

    @RequestMapping("/basket")
    public ModelAndView showUserBasket(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("products", basketService.getBasketForUser(principal.getName()));
        modelAndView.setViewName("basket");

        return modelAndView;
    }

    @RequestMapping("/insertToBasket/{id}")
    public ModelAndView insertToBasket(Principal principal, @PathVariable Long id, @RequestParam String size) {

        basketService.insertProductToBasket(id, principal.getName(), size);
        return new ModelAndView("redirect:/details/" + id);
    }

    @RequestMapping("/acceptOrder")
    public ModelAndView acceptOrder(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();

        basketService.acceptOrder(principal.getName());

        modelAndView.setViewName("basket");
        modelAndView.addObject("success", "");

        return modelAndView;
    }
}
