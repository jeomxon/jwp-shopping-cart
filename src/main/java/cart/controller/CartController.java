package cart.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import cart.controller.dto.AuthInfo;
import cart.controller.dto.CartResponse;
import cart.controller.dto.ProductResponse;
import cart.dao.CartDao;
import cart.dao.ProductDao;
import cart.domain.Product;
import cart.infrastructure.BasicAuthorizationExtractor;

@Controller
public class CartController {

    private final ProductDao productDao;
    private final CartDao cartDao;
    private final BasicAuthorizationExtractor basicAuthorizationExtractor = new BasicAuthorizationExtractor();

    public CartController(final ProductDao productDao, final CartDao cartDao) {
        this.productDao = productDao;
        this.cartDao = cartDao;
    }

    @GetMapping("/")
    public String findAllProducts(final Model model) {
        List<Product> products = productDao.findAll();
        List<ProductResponse> productResponses = products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        model.addAttribute("products", productResponses);

        return "index";
    }

    @GetMapping("/cart")
    public String renderCart() {
        return "cart";
    }

    @PostMapping("/cart/products/{productId}")
    public ResponseEntity<Void> addProduct(@PathVariable final Long productId, final HttpServletRequest request) {
        AuthInfo authInfo = basicAuthorizationExtractor.extract(request);
        String email = authInfo.getEmail();
        String password = authInfo.getPassword();

        cartDao.saveCartItemByMemberEmail(email, productId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/cart/products")
    public ResponseEntity<List<CartResponse>> findAllProductsByMember(final HttpServletRequest request) {
        AuthInfo authInfo = basicAuthorizationExtractor.extract(request);
        String email = authInfo.getEmail();
        String password = authInfo.getPassword();

        List<Product> cartItems = cartDao.findCartItemsByMemberEmail(email);
        List<CartResponse> cartResponses = cartItems.stream()
                .map(CartResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(cartResponses);
    }

    @DeleteMapping("/cart/products/{cartId}")
    public ResponseEntity<Void> removeProduct(@PathVariable final Long cartId) {
        cartDao.deleteCartItemById(cartId);

        return ResponseEntity.ok().build();
    }
}
