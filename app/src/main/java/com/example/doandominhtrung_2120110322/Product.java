package com.example.doandominhtrung_2120110322;

public class Product {
    private String name;
    private String detail;
    private String imageUrl;
    private String price;

    public Product(String name, String detail, String imageUrl, String price) {
        this.name = name;
        this.detail = detail;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }
}
