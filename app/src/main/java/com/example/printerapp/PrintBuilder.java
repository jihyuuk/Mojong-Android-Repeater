package com.example.printerapp;

import com.example.printerapp.dto.SaleDTO;
import com.example.printerapp.dto.SaleItemDTO;

import java.text.DecimalFormat;

public class PrintBuilder {

    private StringBuilder sb;
    private SaleDTO saleDTO;
    private DecimalFormat decimalFormat;

    private final String ESCAPE;
    private final int WIDTH;

    public PrintBuilder() {
        sb = new StringBuilder();
        decimalFormat = new DecimalFormat("###,###");
        ESCAPE = new String(new byte[]{0x1b, 0x7c});
        WIDTH = 32;
    }

    public String build(SaleDTO saleDTO){
        this.saleDTO = saleDTO;

        appendTitle();
        appendHeader();
        appendTableHeader();
        appendTableBody();
        appendDiscount();
        appendFooter();

        return sb.toString();
    }

    private void append(String str) {
        sb.append(str);
    }

    private void appendTitle() {
        setNormal();
        setFontTitle();
        alignCenter("영 수 증\n\n");
        setNormal();
    }

    private void appendHeader() {
        append("상호:(주)그린아그로\n");
        append("대표:황용순\n");
        append("주소:인천시 계양구 벌말로 596-3\n");
        append("전화번호:032-132-1423\n");
        append("일시:2024-03-14 20:17\n\n");
    }

    private void appendTableHeader() {
        appendLine();
        appendBold("상 품 명           수 량   금 액\n");
        appendLine();
    }

    private void appendTableBody() {

        for (SaleItemDTO item : saleDTO.getItems()) {

            //상품명
            appendColName(item);
            appendBlank(2);

            //수량
            appendColQuantity(item);
            appendBlank(2);

            //합계
            appendColTotal(item);
        }

        append("\n");
    }

    private void appendDiscount() {
        //할인 x
        if(saleDTO.getSalePrice() <= 0) return;

        //합계금액
        String totalPrice = decimalFormat.format(saleDTO.getTotalPrice());
        //할인금액
        String salePrice = "-"+decimalFormat.format(saleDTO.getSalePrice());

        appendLine();
        append("합 계 금 액");
        appendBlank(WIDTH-13-totalPrice.length());
        append(totalPrice+"원\n");

        append("할 인 금 액");
        appendBlank(WIDTH-13-salePrice.length());
        append(salePrice+"원\n");
    }

    private void appendFooter() {
        appendLine();

        //결제금액
        String finalPrice = decimalFormat.format(saleDTO.getFinalPrice());

        appendBold("결 제 금 액");
        appendBlank(WIDTH-13-finalPrice.length());
        appendBold(finalPrice+"원\n\n\n\n\n");
    }

    private void appendColName(SaleItemDTO item) {
        String name = item.getName();
        int nameSize = 0;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if(c >= '가' && c <= '힣'){
                nameSize += 2;
            }else{
                nameSize += 1;
            }
        }
        append(name);
        if(nameSize < 18){
            appendBlank(18-nameSize);
        }
    }

    private void appendColQuantity(SaleItemDTO item) {
        String quantity = item.getQuantity()+"";
        if(quantity.length() < 3){
            appendBlank(3-quantity.length());
        }
        append(quantity);
    }

    private void appendColTotal(SaleItemDTO item) {
        String total = decimalFormat.format(item.getPrice()* item.getQuantity());
        if(total.length() < 7){
            appendBlank(7-total.length());
        }
        append(total+"\n");
    }

    private void appendLine() {
        append("--------------------------------");
    }

    private void setEsc(String code) {
        append(ESCAPE + code);
    }

    private void setNormal() {
        setEsc("N");
    }

    private void setFontTitle() {
        setEsc("4C");
    }

    private void appendBlank(int count){
        for(int i = 0; i < count; i++){
            append(" ");
        }
    }

    private void appendBold(String str) {
        setEsc("bC");
        append(str);
        setNormal();
    }

    private void alignCenter(String str) {
        setEsc("cA");
        append(str);
        setNormal();
    }

    private void alignRight(String str){
        setEsc("rA");
        append(str);
        setNormal();
    }

}
