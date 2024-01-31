package tacos.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tacos.Taco;
import tacos.TacoOrder;
import tacos.data.TacoRepository;
import tacos.data.OrderRepository;

@Service
public class TacoOrderService {

    private final OrderRepository orderRepo;
    private final TacoRepository tacoRepository;

    public TacoOrderService(OrderRepository orderRepo, TacoRepository tacoRepository) {
        this.orderRepo = orderRepo;
        this.tacoRepository = tacoRepository;
    }

    @Transactional
    public void placeOrder(TacoOrder order) {
        // Extract taco names from the order
    	 String tacoNames = order.getTacoNames();
        
        System.out.println("Taco names being saved: " + tacoNames);
        
        // Set the taco names in the order
        order.setTacoNames(tacoNames);

        // Save the order, now including the taco names
        TacoOrder savedOrder = orderRepo.save(order);

        // Associate each Taco with the savedOrder and save it
        for (Taco taco : order.getTacos()) {
            taco.setTacoOrder(savedOrder);
            tacoRepository.save(taco);
        }
}
}
