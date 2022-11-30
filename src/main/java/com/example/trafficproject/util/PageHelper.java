package com.example.trafficproject.util;

import org.springframework.data.domain.Sort;

import java.util.List;

public class PageHelper {
    public static String orderBy(Sort sort){
        if(sort.isEmpty()){
            return "id DESC";
        }
        List<String> orderBys = sort.stream()
                .map(order -> order.getProperty() + " " + order.getDirection())
                .toList();
        return String.join(", ",orderBys);
    }
}
