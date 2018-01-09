package cf.aditya.shoppingapp.data;

/**
 * Created by HP on 31-12-2017.
 */

public class CartItem {
    private int id;
    private int productId;
    private int quantity;
    private double discountedprice;
    private String name;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = "https://adityapahansu.000webhostapp.com/panel/uploads/images/products/tiny/"+image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getDiscountedprice() {
        return discountedprice;
    }

    public void setDiscountedprice(double discountedprice) {
        this.discountedprice = discountedprice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
