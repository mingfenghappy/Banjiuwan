package com.ins.middle.common;


import android.graphics.Color;
import android.support.annotation.ColorInt;

import com.ins.middle.entity.User;
import com.sobey.common.utils.ApplicationHelp;
import com.sobey.common.utils.PreferenceUtil;

/**
 * 该类封装了app中所有静态数据和持久化数据的读写操作
 * 所有持久化数据都保存在preferences文件中，包括简单数据类型，和复杂数据类型
 * PreferenceUtil提供了保存复杂对象的方法，复杂数据类型需实现Serializable接口
 *
 * @author Administrator
 */
public class AppData {

    public static class App {

        private static final String KEY_STRARUP = "startup";
        private static final String KEY_VERSIONCODE = "versioncode";
        private static final String KEY_TOKEN = "token";
        private static final String KEY_JPUSHID = "jpushid";
        private static final String KEY_PHONE = "phone";
        private static final String KEY_DOMAIN = "domain";
        private static final String KEY_USER = "user";

        public static int getVersionCode() {
            String versioncode = PreferenceUtil.getString(ApplicationHelp.getApplicationContext(), KEY_VERSIONCODE);
            if (versioncode == null || "".equals(versioncode)) {
                return 0;
            } else {
                return Integer.parseInt(versioncode);
            }
        }

        public static void saveVersionCode(int versioncode) {
            PreferenceUtil.saveString(ApplicationHelp.getApplicationContext(), KEY_VERSIONCODE, versioncode + "");
        }

        public static void removeVersionCode() {
            PreferenceUtil.remove(ApplicationHelp.getApplicationContext(), KEY_VERSIONCODE);
        }

        public static String getToken() {
            String token = PreferenceUtil.getString(ApplicationHelp.getApplicationContext(), KEY_TOKEN);
            return token;
        }

        public static void saveToken(String token) {
            PreferenceUtil.saveString(ApplicationHelp.getApplicationContext(), KEY_TOKEN, token);
        }

        public static void removeToken() {
            PreferenceUtil.remove(ApplicationHelp.getApplicationContext(), KEY_TOKEN);
        }

        public static String getDomain() {
            String token = PreferenceUtil.getString(ApplicationHelp.getApplicationContext(), KEY_DOMAIN);
            return token;
        }

        public static void saveDomain(String phone) {
            PreferenceUtil.saveString(ApplicationHelp.getApplicationContext(), KEY_DOMAIN, phone);
        }

        public static void removeDomain() {
            PreferenceUtil.remove(ApplicationHelp.getApplicationContext(), KEY_DOMAIN);
        }

        public static void saveUser(User user) {
            PreferenceUtil.saveObject(ApplicationHelp.getApplicationContext(), KEY_USER, user);
        }

        public static User getUser() {
            return (User) PreferenceUtil.readObject(ApplicationHelp.getApplicationContext(), KEY_USER);
        }

        public static void removeUser() {
            PreferenceUtil.remove(ApplicationHelp.getApplicationContext(), KEY_USER);
        }

        public static String getJpushId() {
            String token = PreferenceUtil.getString(ApplicationHelp.getApplicationContext(), KEY_JPUSHID);
            return token;
        }

        public static void saveJpushId(String token) {
            PreferenceUtil.saveString(ApplicationHelp.getApplicationContext(), KEY_JPUSHID, token);
        }

        public static void removeJpushId() {
            PreferenceUtil.remove(ApplicationHelp.getApplicationContext(), KEY_JPUSHID);
        }

        //////////////

        public static void removeOrderInfo(String key) {
            PreferenceUtil.remove(ApplicationHelp.getApplicationContext(), key);
        }

        ///////////////保存客服电话
        public static String getPhone() {
            String token = PreferenceUtil.getString(ApplicationHelp.getApplicationContext(), KEY_PHONE);
            return token;
        }

        public static void savePhone(String username) {
            PreferenceUtil.saveString(ApplicationHelp.getApplicationContext(), KEY_PHONE, username);
        }

        public static void removePhone() {
            PreferenceUtil.remove(ApplicationHelp.getApplicationContext(), KEY_PHONE);
        }
    }

    /**
     * 记录了app中所有全局控制常量
     */
    public static class Config {
        public static boolean showVali = false;                 //显示验证码（仅测试）
        public static boolean showTestToast = false;            //打印测试信息到窗口（仅测试）
        //设备类型，供web获取
        public static int DEVICE_TYPE = 0;
        public static String ERROR_PAGE_TITLE = "error.html";
        public static String ERROR_PAGE_URL = "file:///android_asset/error.html";
        public static final String JS_BRIDGE_NAME="JSBridge";
    }

    /**
     * 记录了app中所有的请求连接地址
     */
    public static class Url {

        /**
         * 服务器域名
         */
        public static String domain = "http://192.168.118.206:8080/Banjiuwan/";                                //外网测试服务器(二)

        /**
         * 接口请求地址
         */
        public static String version_feast              = domain + "updateAPK/version_feast.json";                                    //客户端检查更新
        public static String version_chef               = domain + "updateAPK/version_chef.json";                                     //厨师端检查更新
        public static String getInfo                    = domain + "mobile/user/getInfo";                                                //token登陆
        public static String app_home                   = domain + "app/page/index";
        public static String app_cart                   = domain + "app/page/car";
        public static String app_find                   = domain + "app/page/find";
        public static String app_customer_service       = domain + "app/page/customer";
        public static String app_mine                   = domain + "app/page/my";
        public static String app_setMeal                = domain + "app/page/setMealDetail";
        public static String app_bamYan                 = domain + "app/page/bamYanDetail";
        public static String app_dinner                 = domain + "app/page/dinnerDetail";
        public static String queryByCategory            = domain + "app/food/queryByCategory";

        public static String sign                       = domain + "app/aliPay/sign";             							        //请求支付宝支付签名
        public static String signWeixin                 = domain + "app/wxPay/sign";             							        //请求微信支付签名

        ///////////////////////////////
        //////////////  厨师端
        ///////////////////////////////

        //厨师端入口页
        public static String FEAST_CHEF_HOMEPAGE         = domain + "/app/page/cookLogin";
        /*我的*/
        public static String FEAST_CHEF_MINE             = domain + "/app/page/cookMy";
        /*我的订单*/
        public static String FEAST_CHEF_MINE_ORDERFORM   = domain + "/app/page/cookMyOrder";
    }


    /**
     * 启动卡片式页面的辅助枚举，携带url拦截关键字以及用于拼装跳转URL的baseUrl
     */
    public enum CardDetail{
        setMeal {
            @Override
            public String getTag() {
                return "setMeal";
            }

            @Override
            public String getBaseUrl() {
                return Url.app_setMeal;
            }

            @Override
            public String getTitle() {
                return "套餐";
            }

            @Override
            public int getBgColor() {
                return Color.parseColor("#9B1D15");
            }
        },
        bamYan {
            @Override
            public String getTag() {
                return "bamYan";
            }

            @Override
            public String getBaseUrl() {
                return Url.app_bamYan;
            }

            @Override
            public String getTitle() {
                return "坝坝宴";
            }

            @Override
            public int getBgColor() {
                return Color.parseColor("#FF5B3D");
            }
        },
        dinner {
            @Override
            public String getTag() {
                return "dinner";
            }

            @Override
            public String getBaseUrl() {
                return Url.app_dinner;
            }

            @Override
            public String getTitle() {
                return "伴餐演奏";
            }

            @Override
            public int getBgColor() {
                return Color.parseColor("#1A54B1");
            }
        };
        public abstract String getTag();
        public abstract String getBaseUrl();
        public abstract String getTitle();
        public abstract @ColorInt int getBgColor();
    }
}
