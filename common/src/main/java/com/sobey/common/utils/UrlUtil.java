package com.sobey.common.utils;

import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * author 边凌
 * date 2017/3/1 9:29
 * desc ${Url工具类}
 */

public class UrlUtil {
    private UrlUtil() {
        throw new UnsupportedOperationException();
    }

    public static
    @Nullable
    String getQueryString(String urlString, String key) {
        return Uri.parse(urlString).getQueryParameter(key);
    }

    //删除url中的服务器域名
    public static String cutDomain(String url) {
        int index = url.indexOf("/", 10);
        if (index >= 0) {
            return url.substring(index);
        } else {
            return url;
        }
    }

    //删除url中的参数
    public static String cutParam(String url) {
        int index = url.indexOf("?");
        if (index >= 0) {
            return url.substring(0, index);
        } else {
            return url;
        }
    }


    /**
     * 判断两个url是否相同（排除domain头的影响）
     */
    public static boolean matchUrl(String url1, String url2) {
        String nourl1 = cutParam(cutDomain(url1));
        if (url2.contains(nourl1)) {
            return true;
        }
        String nourl2 = cutParam(cutDomain(url2));
        if (url1.contains(nourl2)) {
            return true;
        }
        return false;
    }
}
