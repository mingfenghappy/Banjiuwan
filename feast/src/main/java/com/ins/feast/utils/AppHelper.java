package com.ins.feast.utils;

import android.os.Handler;

import com.baidu.mapapi.model.LatLng;
import com.dd.CircularProgressButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ins.feast.entity.Address;
import com.ins.feast.entity.Area;
import com.ins.feast.entity.AreaData;
import com.ins.feast.entity.CategoryConfig;
import com.ins.feast.entity.Position;
import com.ins.middle.common.AppData;
import com.ins.feast.entity.Card;
import com.shelwee.update.utils.VersionUtil;
import com.sobey.common.utils.ApplicationHelp;
import com.sobey.common.utils.StrUtils;
import com.sobey.common.utils.UrlUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/9.
 */
public class AppHelper {

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


    public interface ProgressCallback {

        void callback();
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
            Map<String, String> map = new Gson().fromJson(area.getMapList(), new TypeToken<Map<String, String>>() {
            }.getType());
            List<LatLng> latLngs = MapHelper.map2LatLngs(map);
            area.setLatLngs(latLngs);
        }
    }

    public static boolean chouldEnter(AreaData areaData, String url, LatLng latLng) {
        if (areaData == null) {
            //如果没有配置，（可能是没获取到）那么可以进入
            return true;
        }
        if (StrUtils.isEmpty(areaData.getAreas())) {
            //如果配置区域没有划分，那么可以进入
            return true;
        }
        if (UrlUtil.matchUrl(url, AppData.Url.cookbook)) {
            for (CategoryConfig config : areaData.getConfigs()) {
                if (config.getId() == 1) {
                    //检查当前分类的配置是否可以进入详情
                    return needEnter(config, areaData.getAreas(), latLng);
                }
            }
            //没有配置，默认可以进入
            return true;
        } else if (UrlUtil.matchUrl(url, AppData.Url.setMeal)) {
            for (CategoryConfig config : areaData.getConfigs()) {
                if (config.getId() == 2) {
                    return needEnter(config, areaData.getAreas(), latLng);
                }
            }
            return true;
        } else if (UrlUtil.matchUrl(url, AppData.Url.bamYan)) {
            for (CategoryConfig config : areaData.getConfigs()) {
                if (config.getId() == 3) {
                    return needEnter(config, areaData.getAreas(), latLng);
                }
            }
            return true;
        } else if (UrlUtil.matchUrl(url, AppData.Url.wedding)) {
            for (CategoryConfig config : areaData.getConfigs()) {
                if (config.getId() == 4) {
                    return needEnter(config, areaData.getAreas(), latLng);
                }
            }
            return true;
        } else if (UrlUtil.matchUrl(url, AppData.Url.dinner)) {
            for (CategoryConfig config : areaData.getConfigs()) {
                if (config.getId() == 5) {
                    return needEnter(config, areaData.getAreas(), latLng);
                }
            }
            return true;
        } else if (UrlUtil.matchUrl(url, AppData.Url.serviceDetail)) {
            for (CategoryConfig config : areaData.getConfigs()) {
                if (config.getId() == 6) {
                    return needEnter(config, areaData.getAreas(), latLng);
                }
            }
            return true;
        } else {
            //其他无关链接，可以进入
            return true;
        }
    }

    //判断当前菜品，用户所在位置是否可以进入详情
    public static boolean needEnter(CategoryConfig config, List<Area> areas, LatLng latLng) {
        if (config.getIsInside() == 1) {
            //需要检查围栏
            if (MapHelper.isInAreas4Areas(areas, latLng)) {
                //在围栏中，可以进入
                return true;
            } else {
                //围栏外，不能进入
                return false;
            }
        } else {
            //不需要检查围栏，可以进入
            return true;
        }
    }
}
