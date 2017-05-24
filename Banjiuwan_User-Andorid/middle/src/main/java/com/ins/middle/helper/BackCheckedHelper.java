package com.ins.middle.helper;

import android.util.Log;

import com.sobey.common.utils.StrUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 * url返回栈
 */

public class BackCheckedHelper {

    //Stack 栈 无法移除非栈顶元素
    //TreeSet 有序不重复
    //LinkedList 基于链表，通过控制add remove方法模拟一个可增删的栈
    //维护一个静态list，app被回收之后，list为空相当于清空了返回栈
    private static List<String> backUrls = new LinkedList<String>();

    public static void addBackUrl(String url) {
        int index = isExist(backUrls, url);
        if (index == -1) {
            //返回栈没有该链接
            backUrls.add(url);
        } else {
            //返回栈已经存在该链接了
            String remove = backUrls.remove(index);
            backUrls.add(remove);
        }
        printUrls();
    }

    public static String getBackUrl() {
        if (StrUtils.isEmpty(backUrls) || backUrls.size() <= 1) {
            return null;
        } else {
            String remove = backUrls.get(backUrls.size() - 1 -1);
            backUrls.remove(backUrls.size() - 1);
            return remove;
        }
    }

    //查找列表中是否已经存在某个特定的url
    //存在返回该元素index ，不存在返回-1
    private static int isExist(List<String> backUrls, String url) {
        if (StrUtils.isEmpty(backUrls) || StrUtils.isEmpty(url)) {
            return -1;
        }
        for (int i = 0; i < backUrls.size(); i++) {
            if (backUrls.get(i).equals(url)) {
                return i;
            }
        }
        return -1;
    }

    public static void printUrls() {
        if (StrUtils.isEmpty(backUrls)) {
            Log.e("liao", "backUrls:null");
        }
        for (String url : backUrls) {
            Log.e("liao", url);
        }
    }
}
