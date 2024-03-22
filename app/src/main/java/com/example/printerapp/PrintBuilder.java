package com.example.printerapp;

import com.example.printerapp.dto.Sale;
import com.example.printerapp.dto.SaleItem;

import java.text.DecimalFormat;

public class PrintBuilder {

    private StringBuilder sb;
    private Sale sale;
    private DecimalFormat decimalFormat;

    private final String ESCAPE;
    private final int WIDTH;

    public PrintBuilder() {
        sb = new StringBuilder();
        decimalFormat = new DecimalFormat("###,###");
        ESCAPE = new String(new byte[]{0x1b, 0x7c});
        WIDTH = 32;
    }

    public String build(Sale sale){
        this.sale = sale;

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
        append("전화번호:032-544-8228\n");
        append("판매변호:#"+sale.getId()+"\n");
        append("일시:"+sale.getTime()+"\n\n");
    }

    private void appendTableHeader() {
        appendLine();
        appendBold("상 품 명           수 량   금 액\n");
        appendLine();
    }

    private void appendTableBody() {

        for (SaleItem item : sale.getItems()) {

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
        if(sale.getSalePrice() <= 0) return;

        //합계금액
        String totalPrice = decimalFormat.format(sale.getTotalPrice());
        //할인금액
        String salePrice = "-"+decimalFormat.format(sale.getSalePrice());

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
        String finalPrice = decimalFormat.format(sale.getFinalPrice());

        appendBold("결 제 금 액");
        appendBlank(WIDTH-13-finalPrice.length());
        appendBold(finalPrice+"원\n\n\n\n\n");
    }

    private void appendColName(SaleItem item) {
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

    private void appendColQuantity(SaleItem item) {
        String quantity = item.getQuantity()+"";
        if(quantity.length() < 3){
            appendBlank(3-quantity.length());
        }
        append(quantity);
    }

    private void appendColTotal(SaleItem item) {
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
