package com.lee.versioncheck.util;

import android.support.annotation.Keep;

import com.google.gson.Gson;

/**
 * CreateDate：18-10-30 on 下午5:42
 * Describe:
 * Coder: lee
 */
@Keep
public class GsonInner {

    private static Gson gson;

    public static Gson getInstance() {
        if (null == gson)
            gson = new Gson();
        return gson;
    }
}
