package pl.polsl.aei.sklep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.aei.sklep.dto.ProductOnListDTO;
import pl.polsl.aei.sklep.repository.*;
import pl.polsl.aei.sklep.repository.entity.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BasketServiceImpl implements BasketService {

    private UserRepository userRepository;

    private ProductOrderRepository productOrderRepository;

    private ProductRepository productRepository;

    private OrderRepository orderRepository;

    private WarehouseRepository warehouseRepository;

    @Autowired
    public void setWarehouseRepository(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setProductOrderRepository(ProductOrderRepository productOrderRepository) {
        this.productOrderRepository = productOrderRepository;
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void insertProductToBasket(Long productId, String username) {
        productRepository
                .findById(productId)
                .ifPresent(e -> {
                    User user = userRepository.findUserByUsername(username);

                    Order userOrder = orderRepository.findOrderByUserAndOrderDateIsNull(user);

                    if (userOrder == null) {
                        userOrder = new Order();
                        userOrder.setTotalCost(BigDecimal.ZERO);
                        userOrder.setUser(user);
                        user.setOrders(Collections.singleton(userOrder));
                        orderRepository.save(userOrder);
                    }

                    Warehouse warehouse = e.getWarehouse().iterator().next();
                    warehouse.setQuantity(warehouse.getQuantity() - 1);

                    ProductOrder productOrder = new ProductOrder();
                    productOrder.setQuantity(1L);
                    productOrder.setOrder(userOrder);

                    productOrder.setWarehouse(warehouse);

                    userOrder.setTotalCost(userOrder.getTotalCost().add(warehouse.getSaleCost()));

                    productRepository.save(e);
                    productOrderRepository.save(productOrder);
                });
    }

    @Override
    public List<ProductOnListDTO> getBasketForUser(String userName) {
        User user = userRepository.findUserByUsername(userName);
        Order order = orderRepository.findOrderByUserAndOrderDateIsNull(user);

        if (order == null)
            return Collections.emptyList();

        return order.getProductOrder()
                .stream()
                .map(e -> Product.productOnListMapper(e.getWarehouse().getProduct()))
                .collect(Collectors.toList());
    }

    @Override
    public void acceptOrder(String username) {
        User user = userRepository.findUserByUsername(username);
        Order order = orderRepository.findOrderByUserAndOrderDateIsNull(user);
        order.setOrderDate(new Date());

        orderRepository.save(order);
    }
}
