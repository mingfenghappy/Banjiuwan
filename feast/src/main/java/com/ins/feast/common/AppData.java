package com.ins.feast.common;


import com.ins.feast.entity.User;
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
    }

    /**
     * 记录了app中所有的请求连接地址
     */
    public static class Url {

        /**
         * 服务器域名
         */
//		public static String domain = "http://192.168.118.110:9528/Carpooling/";								//客服测试服务器
//		public static String domain = "http://101.201.222.160:8101/Carpooling/";								//外网测试服务器(一)
		public static String domain = "http://139.129.111.76:8101/Carpooling/";								//外网测试服务器(二)
//		public static String domain = "http://192.168.118.110:9528/Carpooling/";								//内部测试服务器
//      public static String domain = "http://192.168.118.196:9527/Carpooling/";                                //开发服务器(谢启谋)
//		public static String domain = "http://192.168.0.119:8080/Carpooling/";								//开发服务器(李作焕)

        /**
         * 接口请求地址
         */
        public static String version_passenger        = domain + "updateAPK/version_passenger.json";	     						    //检查更新
        public static String getInfo			    	= domain + "mobile/user/getInfo";												//token登陆
    }
}
