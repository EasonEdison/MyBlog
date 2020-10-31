package com.example.demo.bean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TTTT {
    public static void main(String[] args) {
        String a = "asdajj";
        boolean jj = a.contains("jaj");
        System.out.println(jj);
        HashMap<Integer, Integer> map = new HashMap<>();
        Set<Integer> set = map.keySet();
        Iterator iterator = map.entrySet().iterator();
        Object next = iterator.next();
    }
}
