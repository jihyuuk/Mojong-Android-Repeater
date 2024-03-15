package com.example.printerapp.dto;

import java.util.List;


public class SaleDTO {
    
    //장바구니 아이템들
    private List<SaleItemDTO> items;
    //합계
    private int totalPrice;
    //할인금액
    private int salePrice;
    //최종금액
    private int finalPrice;
    //결제수단
    private String pay;


    public List<SaleItemDTO> getItems() {
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

    public String getPay() {
        return pay;
    }
}
