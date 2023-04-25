package cart.controller;

import cart.controller.dto.ModifyProductRequest;
import cart.controller.dto.ProductDto;
import cart.dao.ProductDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductDao productDao;

    public AdminController(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @GetMapping
    public String findProduct(Model model) {
        List<ProductDto> productDtos = productDao.findAll();
        model.addAttribute("products", productDtos);

        return "admin";
    }

    @PutMapping("/products/{productId}")
    public String modifyProduct(@PathVariable Long productId, @RequestBody ModifyProductRequest modifyProductRequest) {
        productDao.update(productId, modifyProductRequest);

        return "admin";
    }
}
