package com.example.doandominhtrung_2120110322;
public class Product {
    private String name;
    private String description;
    private int imageResId;
    private String price;

    public Product(String name, String description, int imageResId, String price) {
        this.name = name;
        this.description = description;
        this.imageResId = imageResId;
        this.price = price;
    }

    // Getter
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }
    public String getPrice() { return price; }
}
