package javaTest.lang.util;

import java.util.HashMap;

/**
 * @Author GJXAIOU
 * @Date 2020/2/27 14:39
 */
public class HashMapTest {

    public static void main(String[] args) {
        HashMap<Integer, Integer> map = new HashMap<>(10);
        System.out.println(map.size());
        map.put(1,1);
        System.out.println(map.size());
    }
}
