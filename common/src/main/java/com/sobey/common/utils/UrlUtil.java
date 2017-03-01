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

    public static @Nullable String getQueryString(String urlString, String key) {
        return Uri.parse(urlString).getQueryParameter(key);
    }
}
