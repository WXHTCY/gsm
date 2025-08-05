package cn.edu.guet;

import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
        int b=55;
        m1(3);
    }

    static void m1(int a) {
        System.out.println("a的值是：" + a);
    }
}
