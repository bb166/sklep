package pl.polsl.aei.sklep.service;

import pl.polsl.aei.sklep.dto.ProductOnListDTO;

import java.util.List;

public interface BasketService {
    void insertProductToBasket(Long productId, String username);
    List<ProductOnListDTO> getBasketForUser(String userName);
    void acceptOrder(String username);
}
