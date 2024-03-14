package com.example.printerapp;

import androidx.annotation.NonNull;

public class PrintBuilder {

    private StringBuilder sb;
    private final String ESCAPE;

    public PrintBuilder() {
        sb = new StringBuilder();
        ESCAPE = new String(new byte[]{0x1b, 0x7c});
    }

    public void append(String str) {
        sb.append(str);
    }

    public void appendTitle() {
        setFontTitle();
        alignCenter("영 수 증\n\n");
        setNormal();
    }

    public void appendHeader() {
        append("상호:(주)그린아그로\n");
        append("대표:황용순\n");
        append("주소:인천시 계양구 벌말로 596-3\n");
        append("전화번호:032-132-1423\n");
        append("일시:2024-03-14 20:17\n\n");
    }

    public void appendTableHeader() {
        appendLine();
        appendBold("상 품 명           수 량   금 액\n");
        appendLine();
    }

    public void appendTableBody() {
        append("가나다라마바사아자  999  200,000\n");
        append("올복합               50   25,000\n");
        append("오이고추             10    5,000\n");
        append("대추방울(빨강)       10   10,000\n");
        append("애호박                3    1,500\n\n");

    }

    public void appendDiscount() {
        appendLine();
        append("합 계 금 액            241,500원");
        append("할 인 금 액             -1,500원");
    }

    public void appendFooter() {
        appendLine();
        appendBold("계 산 금 액            240,000원");
    }


    public void appendLine() {
        append("--------------------------------");
    }

    public void setEsc(String code) {
        append(ESCAPE + code);
    }

    public void setNormal() {
        setEsc("N");
    }

    public void setFontTitle() {
        setEsc("4C");
    }

    public void appendBold(String str) {
        setEsc("bC");
        append(str);
        setNormal();
    }

    public void alignCenter(String str) {
        setEsc("cA");
        append(str);
        setNormal();
    }

    @NonNull
    @Override
    public String toString() {

        appendTitle();
        appendHeader();
        appendTableHeader();
        appendTableBody();
        appendDiscount();
        appendFooter();

        return sb.toString();
    }
}
