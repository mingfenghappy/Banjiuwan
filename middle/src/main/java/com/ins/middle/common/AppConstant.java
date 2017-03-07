package com.ins.middle.common;

/**
 * Created by Administrator on 2016/7/1 0001.
 */
public class AppConstant {
    public static final Integer EVENT_UPDATE_ME = 0xfe01;
    public static final Integer EVENT_UPDATE_LOGIN = 0xfe05;
    public static final Integer EVENT_UPDATE_PAYWAY = 0xfe06;


    public static final String EVENT_DIALOGLOGON_VALI = "EVENT_DIALOGLOGON_VALI";
    public static final String EVENT_DIALOGLOGON_PHONE = "EVENT_DIALOGLOGON_PHONE";
    public static final String EVENT_BIND_BANK = "EVENT_BIND_BANK";       //绑定银行卡fragment页面传参
    public static final String EVENT_MODIFYPAYPSW = "EVENT_MODIFYPAYPSW";       //修改提现密码fragment页面传参
    public static final String EVENT_SALE = "EVENT_SALE";       //分享列表fragment页面传参
    public static final String EVENT_HOME_CITY = "EVENT_HOME_CITY";       //首页选择城市
    public static final String EVENT_CASH_MONEY = "EVENT_CASH_MONEY";       //提现余额变化

    private static final String FLAGMODE = "LOVE&INS";

    public static String makeFlagStr(String flag, String str) {
        return flag + FLAGMODE + str;
    }

    public static String getFlag(String strSpc) {
        int i = strSpc.indexOf(FLAGMODE);
        return strSpc.substring(0, i);
    }

    public static String getStr(String strSpc) {
        int i = strSpc.indexOf(FLAGMODE);
        return strSpc.substring(i + FLAGMODE.length());
    }
}
