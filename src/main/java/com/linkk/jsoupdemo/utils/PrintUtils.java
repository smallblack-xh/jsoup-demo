package com.linkk.jsoupdemo.utils;

import java.util.List;

/**
 * @Description:
 * @Author: Administrator
 * @Date: 2019/5/11
 * @Package: com.linkk.jsoupdemo.utils
 * @Name: PrintUtils
 **/
public class PrintUtils {
    public static void printLinks(List<Link> links) {
        for (Link link:links) {
            System.out.println(link);
        }
    }
}
