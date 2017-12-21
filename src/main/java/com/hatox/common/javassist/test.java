package com.hatox.common.javassist;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        List<String> list=new ArrayList<String>();
        list.add("safsda");
        list.add("111111");
        String [] ss=new String[0];
        list.toArray(ss);
        System.out.println(ss);
    }
}
