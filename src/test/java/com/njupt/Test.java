package com.njupt;

/**
 * @author weixk
 * @version Created time 17/5/4. Last-modified time 17/5/4.
 */
public class Test {

    public static void main(String[] args) {
        String s = "aaa; bbb;  ccc   ";
        String a[] = s.split(";\\s*");
        for (String iter : a) {
            System.out.println(iter.trim());
        }
    }
}
