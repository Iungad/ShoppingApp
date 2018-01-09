package cf.aditya.shoppingapp.data;

import android.graphics.Bitmap;

/**
 * Created by HP on 22-12-2017.
 */

public class Product {
    String name;
    int id;
    double price;
    int discount;
    double discountedprice;
    String image;
    String brand;

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public double getDiscountedprice() {
        return discountedprice;
    }

    public void setDiscountedprice(double discountedprice) {
        this.discountedprice = discountedprice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = "https://adityapahansu.000webhostapp.com/panel/uploads/images/products/tiny/"+image;
    }



}
