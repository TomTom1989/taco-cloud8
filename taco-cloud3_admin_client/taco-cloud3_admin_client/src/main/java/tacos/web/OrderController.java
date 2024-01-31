package tacos.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import tacos.TacoOrder;
import tacos.service.TacoOrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

    private final TacoOrderService orderService;

    public OrderController(TacoOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/current")
    public String orderForm(@ModelAttribute TacoOrder tacoOrder, Model model) {
        model.addAttribute("tacoOrder", tacoOrder);
        return "orderForm";
    }


    @PostMapping
    public String processOrder(@Valid @ModelAttribute("tacoOrder") TacoOrder order,
                               Errors errors, SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "orderForm";
        }

        orderService.placeOrder(order);
        sessionStatus.setComplete();

        return "redirect:/";
    }
}
