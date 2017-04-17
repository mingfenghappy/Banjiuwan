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
//        public static String domain = "http://tiger.magic-beans.cn/Banjiuwan/";                                //外网测试服务器(二)
        public static String domain = "http://192.168.118.110:8080/Banjiuwan/";                                //外网测试服务器(二)

        /**
         * 接口请求地址
         */
        public static String version_feast              = domain + "updateAPK/version_feast.json";                                    //客户端检查更新
        public static String version_chef               = domain + "updateAPK/version_chef.json";                                     //厨师端检查更新
        public static String app_home                   = domain + "app/page/index";
        public static String app_cart                   = domain + "app/page/car";
        public static String app_find                   = domain + "app/page/find";
        public static String app_customer_service       = domain + "app/page/customer";
        public static String app_mine                   = domain + "app/page/my";
        public static String app_setMeal                = domain + "app/page/setMealDetail";
        public static String app_bamYan                 = domain + "app/page/bamYanDetail";
        public static String app_dinner                 = domain + "app/page/dinnerDetail";

        public static String moreAddress                = domain + "app/page/moreAddress";                                        //更多地址（新增收获地址页面）
        public static String addAddress                 = domain + "app/page/addAddress";                                         //新增地址（新增收获地址页面）
        public static String myOrder                    = domain + "app/page/myOrder";                                             //我的订单
        public static String loginPageCook              = domain + "app/page/cookLogin";                                           //厨师登录页面
        public static String loginPage                  = domain + "app/page/login";                                                //客户端登录页面
        public static String search                     = domain + "app/page/search";                                              //客户端搜索页面
        public static String addOrder                   = domain + "app/page/orderEdit";                                          //客户端填写订单页面

        public static String cookbook                   = domain + "app/page/cookbook";                                            //客户端点菜页面
        public static String setMeal                    = domain + "app/page/setMeal";                                             //客户端套餐页面
        public static String bamYan                     = domain + "app/page/bamYan";                                              //客户端坝坝宴页面
        public static String wedding                    = domain + "app/page/wedding";                                             //客户端婚庆页面
        public static String dinner                     = domain + "app/page/dinner";                                              //客户半餐演奏页面
        public static String serviceDetail              = domain + "app/page/serviceDetail";                                      //客户专业服务页面
        public static String news                       = domain + "app/page/bannerNews";                                                //客户首页弹窗点击跳转页面，参数newsId

        public static String queryByCategory            = domain + "app/food/queryByCategory";
        public static String getAddress                 = domain + "app/address/getAddress";                                      //获取收货地址
        public static String updateCookLatLng           = domain + "app/cook/updateCookLatLng";                                  //厨师实时上传坐标（5秒一次）
        public static String getCategoryConfig          = domain + "app/category/getCategoryConfig";                            //用户端获取菜品配置（地理围栏）
        public static String sale                       = domain + "app/homeBanner/getBanner";                                   //用户端获取首页推荐接口
        public static String judgeIsPoint               = domain + "app/user/judgeIsPoint";                                      //判断当前用户是否可以点餐

        public static String sign                       = domain + "app/aliPay/sign";             							        //请求支付宝支付签名
        public static String signWeixin                 = domain + "app/wxPay/sign";             							        //请求微信支付签名
        public static String recharge                   = domain + "app/AliPayRecharge/sign";             						//请求支付宝充值签名
        public static String rechargeWeixin             = domain + "app/WeChatPayRecharge/sign";             					    //请求微信充值签名

        ///////////////////////////////
        //////////////  厨师端
        ///////////////////////////////

        //厨师端入口页
        public static String FEAST_CHEF_HOMEPAGE         = domain + "app/page/cookLogin";
        /*我的*/
        public static String FEAST_CHEF_MINE             = domain + "app/page/cookMy";
        /*我的订单*/
        public static String FEAST_CHEF_MINE_ORDERFORM   = domain + "app/page/cookMyOrder";
        /*忘记密码*/
        public static String FEAST_CHEF_FORGETPSW        = domain + "app/page/forgetPassword";


        //源生接口url
        public static String getInfo                    = domain + "app/user/getInfo";                                                  //token登陆
        public static String login                      = domain + "app/user/login";                                                    //登陆
    }


    /**
     * 启动卡片式页面的辅助枚举，携带url拦截关键字以及用于拼装跳转URL的baseUrl等信息
     */
    public enum CardType {
        /*套餐*/
        setMeal {
            @Override
            public String getTag() {
                return Url.setMeal;
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

            @Override
            public String getCategoryId() {
                return "9";
            }
        },
        /*坝坝宴*/
        bamYan {
            @Override
            public String getTag() {
                return Url.bamYan;
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

            @Override
            public String getCategoryId() {
                return "10";
            }
        },
        /*伴餐演奏*/
        dinner {
            @Override
            public String getTag() {
                return Url.dinner;
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

            @Override
            public String getCategoryId() {
                return "12";
            }
        },;

        public abstract String getTag();

        public abstract String getBaseUrl();

        public abstract String getTitle();

        public abstract
        @ColorInt
        int getBgColor();

        /*这个接口用于卡片式页面请求服务器数据的常量参数*/
        public abstract String getCategoryId();
    }
}
