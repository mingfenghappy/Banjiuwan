package com.ins.feast.utils;

import android.os.Handler;
import android.support.annotation.Nullable;

import com.baidu.mapapi.model.LatLng;
import com.dd.CircularProgressButton;
import com.ins.feast.entity.Address;
import com.ins.feast.entity.Area;
import com.ins.feast.entity.AreaData;
import com.ins.feast.entity.Card;
import com.ins.feast.entity.CategoryConfig;
import com.ins.feast.ui.dialog.DialogNotice;
import com.ins.middle.common.AppData;
import com.shelwee.update.utils.VersionUtil;
import com.sobey.common.utils.ApplicationHelp;
import com.sobey.common.utils.L;
import com.sobey.common.utils.PreferenceUtil;
import com.sobey.common.utils.StrUtils;
import com.sobey.common.utils.UrlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class AppHelper {

    private static final String KEY_AREA = "area";

    public static void saveArea(AreaData area) {
        PreferenceUtil.saveObject(ApplicationHelp.getApplicationContext(), KEY_AREA, area);
    }

    public static AreaData getArea() {
        return (AreaData) PreferenceUtil.readObject(ApplicationHelp.getApplicationContext(), KEY_AREA);
    }

    public static void removeArea() {
        PreferenceUtil.remove(ApplicationHelp.getApplicationContext(), KEY_AREA);
    }


    public static boolean getStartUp() {
        int versionCodeSave = AppData.App.getVersionCode();
        int versionCode = VersionUtil.getAppVersionCode(ApplicationHelp.getApplicationContext());
        return versionCode <= versionCodeSave;
    }

    public static void saveStartUp() {
        int versionCode = VersionUtil.getAppVersionCode(ApplicationHelp.getApplicationContext());
        AppData.App.saveVersionCode(versionCode);
    }

    public static void removeStartUp() {
        AppHelper.removeStartUp();
    }

    ////////////////////////////////////////
    ////////////////////////////////////////
    ////////////////////////////////////////

    public static String getRealImgPath(String path) {
        if (!StrUtils.isEmpty(path) && path.startsWith("upload")) {
            return AppData.Url.domain + path;
        } else {
            return path;
        }
    }

    public static void progError2dle(final CircularProgressButton btn_go) {
        btn_go.setProgress(-1);
        handlProgressButton(btn_go, null, 0);
    }

    public static void progOk2dle(final CircularProgressButton btn_go) {
        btn_go.setProgress(100);
        handlProgressButton(btn_go, null, 0);
    }

    public static void progOk(final CircularProgressButton btn_go) {
        btn_go.setProgress(100);
        handlProgressButton(btn_go, null, -2);
    }

    public static void progError2dle(final CircularProgressButton btn_go, final ProgressCallback callback) {
        btn_go.setProgress(-1);
        handlProgressButton(btn_go, callback, 0);
    }

    public static void progOk2dle(final CircularProgressButton btn_go, final ProgressCallback callback) {
        btn_go.setProgress(100);
        handlProgressButton(btn_go, callback, 0);
    }

    public static void progOk(final CircularProgressButton btn_go, final ProgressCallback callback) {
        btn_go.setProgress(100);
        handlProgressButton(btn_go, callback, -2);
    }

    public static void handlProgressButton(final CircularProgressButton btn_go, final ProgressCallback callback, final int value) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (btn_go != null) {
                    btn_go.setClickable(true);
                    if (value != -2) {
                        btn_go.setProgress(value);
                    }
                }
                if (callback != null) {
                    callback.callback();
                }
            }
        }, 1000);
    }

    public static String getStarsStr(int count) {
        if (count == 0) return "";
        String ret = "";
        for (int i = 0; i < count; i++) {
            ret += "★";
        }
        return ret;
    }

    public static void initIndex(List<Card> cards) {
        if (StrUtils.isEmpty(cards)) return;
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            card.setIndex(i);
        }
    }

    public static Address getDefaultAddressInList(List<Address> addresses) {
        if (StrUtils.isEmpty(addresses)) {
            return null;
        }
        for (Address address : addresses) {
            if (address.getIsDefault() == 1) {
                return address;
            }
        }
        return null;
    }

    //设置点餐配置的latLngs对象
    public static void setLatlogEntity(AreaData areaData) {
        if (areaData == null || StrUtils.isEmpty(areaData.getAreas())) {
            return;
        }
        List<Area> areas = areaData.getAreas();
        for (Area area : areas) {
//            Map<String, String> map = new Gson().fromJson(area.getMapList(), new TypeToken<Map<String, String>>() {
//            }.getType());
//            List<LatLng> latLngs = MapHelper.map2LatLngs(map);

            //attention please!!!!!!!!!!!
            //这里不能将mapList解析为Map<String,String>，因为可能出现经度相同，纬度不同的地址，
            //这样服务器返回的数据会包含同键不同值的对，使用Gson会出现解析异常
            //因此这里采用手动解析获取List<LatLng>的方式解决
            List<LatLng> latLngs = convertMapStringToLatLngList(area.getMapList());
            L.d(latLngs);
            area.setLatLngs(latLngs);
        }
    }

    private static List<LatLng> convertMapStringToLatLngList(String mapList) {
        List<LatLng> latLngs = new ArrayList<>();
        String[] latLngPairs = convertMapListToLatLngPair(mapList);
        for (String latLngPair : latLngPairs) {
            LatLng latLng = convertLatLngPairToLatLngArray(latLngPair);
            if (latLng != null) {
                latLngs.add(latLng);
            }
        }
        return latLngs;
    }

    private static LatLng convertLatLngPairToLatLngArray(String latLngPair) {
        try {
            String[] split = latLngPair.split(":");
            split[0] = split[0].replace("\"", "");
            split[1] = split[1].replace("\"", "");
            double[] doubles = new double[2];
            doubles[0] = Double.parseDouble(split[0]);
            doubles[1] = Double.parseDouble(split[1]);
            return new LatLng(doubles[1], doubles[0]);
        } catch (Exception e) {
            ThrowableUtil.handleThrowable(e);
        }
        return null;
    }

    private static String[] convertMapListToLatLngPair(String mapList) {
        try {
            String replace = mapList.replace("{", "").replace("}", "");
            return replace.split(",");
        } catch (Exception e) {
            ThrowableUtil.handleThrowable(e);
        }
        return new String[0];
    }

    //    public static boolean couldEnter(AreaData areaData, String url, LatLng latLng, DialogNotice dialogNotice, int couldOrder) {
//        //如果点餐标准为不能点餐0，
//        if (couldOrder == 0) {
//            if (AppHelper.isCategoryConfigUrl(url)) {
//                dialogNotice.setTypeMsg(DialogNotice.TYPE_WARNING, "您购物车中包含不同类别的商品");
//                return false;
//            }
//        }
//        if (areaData == null) {
//            //如果没有配置，（可能是没获取到）那么不能进入
//            return false;
//        }
//        if (StrUtils.isEmpty(areaData.getAreas())) {
//            //如果配置区域没有划分，那么可以进入
//            return true;
//        }
//        if (StrUtils.isEmpty(areaData.getConfigs())) {
//            //如果没有配置，那么可以进入
//            return true;
//        }
//        //根据拦截链接返回该链接的地理拦截配置，如果该链接不需要拦截返回null
//        CategoryConfig config = getCategoryConfigByUrl(areaData.getConfigs(), url);
//        if (config != null) {
//            //如果是坝坝宴 id = 3 设置不同的Icon
//            if (dialogNotice != null) {
//                int type = config.getId() == 3 ? DialogNotice.TYPE_ERROR : DialogNotice.TYPE_WARNING;
//                String msg = !StrUtils.isEmpty(config.getMsg()) ? config.getMsg() : "对不起！您的点餐不在服务范围内";
//                dialogNotice.setTypeMsg(type, msg);
//            }
//            if (latLng != null) {
//                return canEnter(config, areaData.getAreas(), latLng);
//            } else {
//                //定位失败提示
//                if (dialogNotice != null)
//                    dialogNotice.setTypeMsg(DialogNotice.TYPE_WARNING, "定位失败，请重新定位");
//                return false;
//            }
//        } else {
//            //config为null，不拦截
//            return true;
//        }
//    }
    public static boolean couldEnter(AreaData areaData, String url, LatLng latLng, DialogNotice dialogNotice, int couldOrder) {
        //根据Url判断是否需要进行地理拦截,不需要直接返回true
        L.d(latLng+","+couldOrder);
        if (shouldNotCheckCanEnter(url)) return true;
        //判断当前AreaData是否为空或无数据无配置，满足条件不进行下一步判断
        Boolean x = canAlwaysEnterIfNotConfigAreaData(areaData);
        if (x != null) return x;

        //在需要进行地理拦截的情况下，根据配置返回值并展示相应弹窗
        CategoryConfig categoryConfig = getCategoryConfigByUrl(areaData.getConfigs(), url);
        if (categoryConfig == null) {
            return true;
        }
        boolean canEnter = canEnter(categoryConfig, areaData.getAreas(), latLng);
        if (canEnter) {
            if (couldOrder == 0) {
                showTypeNotMatchedDialog(dialogNotice);
            } else {
                return true;
            }

        } else {
            showNotInRangeDialog(dialogNotice, categoryConfig);
        }
        return false;
    }

    private static void showTypeNotMatchedDialog(DialogNotice dialogNotice) {
        L.d("showTypeNotMatchedDialog");
        dialogNotice.setTypeMsg(DialogNotice.TYPE_WARNING, "您购物车中包含不同类别的商品");
    }

    private static boolean shouldNotCheckCanEnter(String url) {
        L.d("shouldNotCheckCanEnter");
        return !isCategoryConfigUrl(url);
    }

    @Nullable
    private static Boolean canAlwaysEnterIfNotConfigAreaData(AreaData areaData) {
        L.d("canAlwaysEnterIfNotConfigAreaData");
        if (areaData == null) {
            //如果没有配置，（可能是没获取到）那么不能进入
            return false;
        }
        if (StrUtils.isEmpty(areaData.getAreas())) {
            //如果配置区域没有划分，那么可以进入
            return true;
        }
        if (StrUtils.isEmpty(areaData.getConfigs())) {
            //如果没有配置，那么可以进入
            return true;
        }
        return null;
    }

    private static void showNotInRangeDialog(DialogNotice dialogNotice, CategoryConfig config) {
        L.d("showNotInRangeDialog");
        int type = config.getId() == 3 ? DialogNotice.TYPE_ERROR : DialogNotice.TYPE_WARNING;
        String msg = !StrUtils.isEmpty(config.getMsg()) ? config.getMsg() : "对不起！您的点餐不在服务范围内";
        dialogNotice.setTypeMsg(type, msg);
    }

    //根据拦截链接返回该链接的地理拦截配置，如果该链接不需要拦截返回null
    private static CategoryConfig getCategoryConfigByUrl(List<CategoryConfig> configs, String url) {
        //客户端点菜页面
        if (UrlUtil.matchUrl(url, AppData.Url.cookbook)) {
            String type = UrlUtil.getQueryString(url, "type");
            for (CategoryConfig config : configs) {
                //type参数为1代表酒水8，否则表示点餐1
                if ("1".equals(type)) {
                    if (config.getId() == 8) {
                        return config;
                    }
                } else {
                    if (config.getId() == 1) {
                        return config;
                    }
                }
            }
            //客户端套餐页面
        } else if (UrlUtil.matchUrl(url, AppData.Url.setMeal)) {
            for (CategoryConfig config : configs) {
                if (config.getId() == 2) {
                    return config;
                }
            }
            //客户端坝坝宴页面
        } else if (UrlUtil.matchUrl(url, AppData.Url.bamYan)) {
            for (CategoryConfig config : configs) {
                if (config.getId() == 3) {
                    return config;
                }
            }
            //客户端婚庆页面
        } else if (UrlUtil.matchUrl(url, AppData.Url.wedding)) {
            for (CategoryConfig config : configs) {
                if (config.getId() == 4) {
                    return config;
                }
            }
            //客户半餐演奏页面
        } else if (UrlUtil.matchUrl(url, AppData.Url.dinner)) {
            for (CategoryConfig config : configs) {
                if (config.getId() == 5) {
                    return config;
                }
            }
            //客户专业服务页面
        } else if (UrlUtil.matchUrl(url, AppData.Url.serviceDetail)) {
            for (CategoryConfig config : configs) {
                if (config.getId() == 6) {
                    return config;
                }
            }
        }
        return null;
    }

    //根据拦截链接返回该链接的地理拦截配置，如果该链接不需要拦截返回null
    public static boolean isCategoryConfigUrl(String url) {
        if (UrlUtil.matchUrl(url, AppData.Url.cookbook)) {
            //客户端点菜页面
            return true;
        } else if (UrlUtil.matchUrl(url, AppData.Url.setMeal)) {
            //客户端套餐页面
            return true;
        } else if (UrlUtil.matchUrl(url, AppData.Url.bamYan)) {
            //客户端坝坝宴页面
            return true;
        } else if (UrlUtil.matchUrl(url, AppData.Url.wedding)) {
            //客户端婚庆页面
            return true;
        } else if (UrlUtil.matchUrl(url, AppData.Url.dinner)) {
            //客户半餐演奏页面
            return true;
        } else if (UrlUtil.matchUrl(url, AppData.Url.serviceDetail)) {
            //客户专业服务页面
            return true;
        }
        return false;
    }

    //判断当前菜品，用户所在位置是否可以进入详情
    public static boolean canEnter(CategoryConfig config, List<Area> areas, LatLng latLng) {
        L.d("canEnter");
        //标志为1：标示需要检查围栏，在内部才能进入。标志为0：不检查，在任何地方都可以进入
//        if (config.getIsInside() == 1) {
//            //需要检查围栏
//            if (MapHelper.isInAreas4Areas(areas, latLng)) {
//                //在围栏中，可以进入
//                return true;
//            } else {
//                //围栏外，不能进入
//                return false;
//            }
//        } else {
//            //不需要检查围栏，可以进入
//            return true;
//        }

        //需求变动，围栏逻辑从上面注释部分变更为下面部分：
        //标志为1：表示在围栏内部才能进入。标志为0：表示在围栏外部才能进入
        if (config.getIsInside() == 1) {
            //只能在围栏内进入
            if (MapHelper.isInAreas4Areas(areas, latLng)) {
                //在围栏中，可以进入
                return true;
            } else {
                //围栏外，不能进入
                return false;
            }
        } else {
            //只能在围栏外进入
            if (MapHelper.isInAreas4Areas(areas, latLng)) {
                //在围栏中，不能进入
                return false;
            } else {
                //围栏外，可以进入
                return true;
            }
        }
    }

    public interface ProgressCallback {

        void callback();
    }
}
