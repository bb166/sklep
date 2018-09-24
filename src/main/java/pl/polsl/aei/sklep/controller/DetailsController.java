package pl.polsl.aei.sklep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.polsl.aei.sklep.dto.OpinionDTO;
import pl.polsl.aei.sklep.dto.ProductDetailsDTO;
import pl.polsl.aei.sklep.dto.WarehouseDTO;
import pl.polsl.aei.sklep.service.OpinionService;
import pl.polsl.aei.sklep.service.ProductService;

import java.io.IOException;
import java.security.Principal;

@Controller
public class DetailsController {

    private ProductService productService;

    private OpinionService opinionService;

    @Autowired
    public DetailsController(ProductService productService, OpinionService opinionService) {
        this.productService = productService;
        this.opinionService = opinionService;
    }

    @RequestMapping(value = "/details/{id}")
    public ModelAndView showProductDetails(@PathVariable Long id) {

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("prod", productService.getDetailsById(id).get());
        modelAndView.addObject("opinions", opinionService.getOpinionsForProduct(id));
        modelAndView.addObject("size", productService.getSizeAvailableById(id).get());
        modelAndView.addObject("id", id);
        modelAndView.setViewName("productDetails");

        return modelAndView;
    }

    @RequestMapping(value = "/addOpinion", method=RequestMethod.POST)
    public String addOpinion(Principal principal, OpinionDTO opinionDTO, RedirectAttributes redirectAttributes) {
        opinionDTO.setUser(principal.getName());

        opinionService.addOpinionToProduct(opinionDTO);

        return "redirect:/details/"+opinionDTO.getProductId();
    }

    @RequestMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }

    @RequestMapping("/addProduct/{category}")
    public ModelAndView showAddProduct(@PathVariable String category) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("addProduct");
        modelAndView.addObject("category",category);

        return modelAndView;
    }

    @RequestMapping("/addWarehouse/{productId}")
    public ModelAndView showAddWarehouse(@PathVariable Long productId) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("addWarehouse");
        modelAndView.addObject("productId", productId);

        return modelAndView;
    }

    @RequestMapping(value = "/addWarehouse", method = RequestMethod.POST)
    public String addWarehouse(WarehouseDTO dto, Long productId) {
        productService.insertWarehouseToProduct(productId, dto);

        return "redirect:/details/"+productId;
    }

    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public ModelAndView addProduct(MultipartFile imagef,
                                   ProductDetailsDTO productDetailsDTO,
                                   String category,
                                   RedirectAttributes redirectAttributes)
            throws IOException {
        ModelAndView modelAndView = new ModelAndView();

        byte[] image = imagef.getBytes();
        productService.insertProductToGroup(category, productDetailsDTO, image);
        redirectAttributes.addAttribute("category", category);
        modelAndView.setViewName("redirect:/categories");

        return modelAndView;
    }
}
