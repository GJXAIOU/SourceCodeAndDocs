package javaTest.lang.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author GJXAIOU
 * @Date 2020/2/26 10:33
 */
public class ArrayListTest {
    public static void main(String[] args) {
        Father[] fathers = new Son[]{};        // 打印结果为class [Lcom.coolcoding.code.Son;
        System.out.println(fathers.getClass());
        List<String> strList = new MyList();
        //打印结果为class [Ljava.lang.String;
        System.out.println(strList.toArray().getClass());
    }
}

class Father {
}

class Son extends Father {
}

class MyList extends ArrayList<String> {
    /**
     * 子类重写父类的方法，返回值可以不一样
     * 但这里只能用数组类型，换成Object就不行
     * 应该算是java本身的bug
     */
    // 为了方便举例直接写死
    @Override
    public String[] toArray() {
        return new String[]{"1", "2", "3"};
    }
}


