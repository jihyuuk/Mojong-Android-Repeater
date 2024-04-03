package com.example.printerapp.dto;

public class SaleItem {

    //상품이름,단가,수량,총합계
    private String name;
    private int quantity;
    private int price;
    private int total;


    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotal() {
        return total;
    }
}
