package com.ins.chef.common;

import com.sobey.common.utils.ApplicationHelp;
import com.sobey.common.utils.PreferenceUtil;

/**
 * author 边凌
 * date 2017/2/27 10:21
 * desc ${厨师端常量类}
 */

public class AppData {
    public static class Url {
        static String DOMAIN = "http://192.168.118.206:8080";
        /**
         * 厨师端入口页
         */
        public final static String FEAST_CHEF_HOMEPAGE = DOMAIN + "/Banjiuwan/app/page/cookLogin";
        /**
         * 厨师端"我的"
         */
        public final static String FEAST_CHEF_MINE=DOMAIN+"/Banjiuwan/app/page/cookMy";
        public final static String FEAST_CHEF_MINE_ORDERFORM=DOMAIN+"/Banjiuwan/app/page/cookMyOrder";

    }
    public static class App {
        private static final String KEY_JPUSHID = "jpushid";

        public static String getJpushId() {
            String token = PreferenceUtil.getString(ApplicationHelp.getApplicationContext(), KEY_JPUSHID);
            return token;
        }
        public static void saveJpushId(String token) {
            PreferenceUtil.saveString(ApplicationHelp.getApplicationContext(), KEY_JPUSHID, token);
        }
    }
}
