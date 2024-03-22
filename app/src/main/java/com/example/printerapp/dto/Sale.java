package com.example.printerapp.dto;

import java.util.List;


public class Sale {

    //판매번호
    private Long id;
    //장바구니 아이템들
    private List<SaleItem> items;
    //합계
    private int totalPrice;
    //할인금액
    private int salePrice;
    //최종금액
    private int finalPrice;
    //판매시간
    private String time;


    public List<SaleItem> getItems() {
        return items;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public Long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }
}
