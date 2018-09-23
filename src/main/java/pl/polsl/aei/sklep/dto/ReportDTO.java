package pl.polsl.aei.sklep.dto;

public class ReportDTO {
    private String productId;
    private String productName;
    private String soldCount;
    private String profit;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(String soldCount) {
        this.soldCount = soldCount;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }
}
