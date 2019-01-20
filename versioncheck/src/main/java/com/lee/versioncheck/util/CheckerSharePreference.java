package com.lee.versioncheck.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.lee.versioncheck.VersionChecker;


public class CheckerSharePreference {

    /**
     * SharePreference 文件名
     */
    private static final String PHONE_INFO_FILE_NAME = "versionChecker";

    /**
     * APP更新的下载续传点
     */
    private static final String PHONE_INFO_LAST_DOWNLOAD_INDEX = "lastDownloadIndex";

    public static void setDownloadIndex(int index) {
        getSharePreferences().edit().putInt(PHONE_INFO_LAST_DOWNLOAD_INDEX, index).apply();
    }

    public static int getDownloadIndex() {
        return getSharePreferences().getInt(PHONE_INFO_LAST_DOWNLOAD_INDEX, 0);
    }

    public static void clear() {
        getSharePreferences().edit().clear().apply();
    }

    private static SharedPreferences getSharePreferences() {
        return VersionChecker.getInstance().getContext().getSharedPreferences(PHONE_INFO_FILE_NAME, Context.MODE_PRIVATE);
    }


}
