package io.github.codingspeedup.execdoc.miners.system;

import java.util.*;

public class SystemMiner {

    public static void main(String[] args) {
        System.out.println("*******************");
        System.out.println("* System.getenv() *");
        System.out.println("*******************");
        System.out.println();
        Map<String, String> foo = getenv();
        List<String> bar = new ArrayList<>(foo.keySet());
        Collections.sort(bar);
        bar.forEach(k -> System.out.println(k + "=" + foo.get(k)));

        System.out.println();
        System.out.println("**************************");
        System.out.println("* System.getProperties() *");
        System.out.println("**************************");
        System.out.println();
        Map<String, String> baz = getProperties();
        bar = new ArrayList<>(baz.keySet());
        Collections.sort(bar);
        bar.forEach(k -> System.out.println(k + "=" + baz.get(k)));
    }

    public static Map<String, String> getenv() {
        Map<String, String> bean = new HashMap<>();
        System.getenv().forEach((k, v) -> bean.put(String.valueOf(k), String.valueOf(v)));
        return bean;
    }

    public static Map<String, String> getProperties() {
        Map<String, String> bean = new HashMap<>();
        System.getProperties().forEach((k, v) -> bean.put(String.valueOf(k), String.valueOf(v)));
        return bean;
    }

}
