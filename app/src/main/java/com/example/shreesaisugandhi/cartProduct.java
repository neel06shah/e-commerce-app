package com.example.shreesaisugandhi;

public class cartProduct {
    public String name, description, quantity, image, mrp, rate, discount,total;

    public cartProduct(){}

    public cartProduct(String name, String description, String quantity, String image, String mrp, String rate, String discount, String total) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.image = image;
        this.mrp = mrp;
        this.rate = rate;
        this.discount = discount;
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String toString()
    {
        return "Name :"+name+"\nDesc :"+description+"\nQuantity :"+quantity+"\nMRP :"+mrp+"\nRate :"+rate+"\nDiscount :"+ discount;
    }
}
