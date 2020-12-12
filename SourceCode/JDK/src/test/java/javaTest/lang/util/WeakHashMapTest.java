package javaTest.lang.util;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @Author GJXAIOU
 * @Date 2020/2/29 20:51
 */
public class WeakHashMapTest {

    public static void main(String[] args) {
        Map<String, Integer> map = new WeakHashMap<>(3);

        // 放入 3 个 new String() 声明的字符串
        map.put(new String("1"), 1);
        map.put(new String("2"), 2);
        map.put(new String("3"), 3);

        // 放入不用 new String() 声明的字符串，这是强引用
        map.put("6", 6);

        // 使用 key 强引用"3"这个字符串
        String key = null;
        for (String s : map.keySet()) {
            // 这个"3"和new String("3")不是一个引用
            if (s.equals("3")) {
                key = s;
            }
        }

        // 输出{6=6, 1=1, 2=2, 3=3}，未 gc 所有 key 都可以打印出来
        System.out.println(map);

        // gc 一下
        System.gc();

        // 放一个 new String() 声明的字符串
        map.put(new String("4"), 4);

        // 输出{4=4, 6=6, 3=3}，gc 后放入的值和强引用的 key 可以打印出来
        System.out.println(map);

        // key与"3"的引用断裂
        key = null;

        // gc 一下
        System.gc();

        // 输出{6=6}，gc 后强引用的 key 可以打印出来
        System.out.println(map);
    }
}
