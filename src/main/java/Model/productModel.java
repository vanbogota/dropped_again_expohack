package Model;

public class productModel {
    private long productId;
    private long partnerId;
    private String description;
    private String name;
    private String price;
    private String promotions;

    public productModel() {
    }

    public productModel(long productId, long partnerId, String descriptions, String name, String price, String promotions) {
        this.productId = productId;
        this.partnerId = partnerId;
        this.description = descriptions;
        this.name = name;
        this.price = price;
        this.promotions = promotions;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }

    public String getDescriptions() {
        return description;
    }

    public void setDescriptions(String descriptions) {
        this.description = descriptions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPromotions() {
        return promotions;
    }

    public void setPromotions(String promotions) {
        this.promotions = promotions;
    }
    @Override
    public String toString() {
        return "productId=" + productId +
                ", partnerId=" + partnerId +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", promotions='" + promotions + '\'' ;
    }

}
