package com.metanit;

import java.util.ArrayList;
import java.util.List;

public class PageWordsCount {
    private String url;
    private List<Integer> list;
    private int total;

    public int getTotal() {
        return total;
    }

    public PageWordsCount(String url) {
        this.url = url;
        total = 0;
        list = new ArrayList<>();
    }

    public void addValue(int v){
        total += v;
        list.add(v);
    }

    public String toString(){
        StringBuilder builder = new StringBuilder(url);
        for (int i = 0; i < list.size(); i++){
            builder.append(' ');
            builder.append(list.get(i));
        }
        builder.append(' ');
        builder.append(total);
        return builder.toString();
    }
}
