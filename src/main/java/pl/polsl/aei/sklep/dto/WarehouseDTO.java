package pl.polsl.aei.sklep.dto;

import java.math.BigDecimal;

public class WarehouseDTO {
    private Long quantity;
    private BigDecimal buyCost;
    private String size;

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getBuyCost() {
        return buyCost;
    }

    public void setBuyCost(BigDecimal buyCost) {
        this.buyCost = buyCost;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
