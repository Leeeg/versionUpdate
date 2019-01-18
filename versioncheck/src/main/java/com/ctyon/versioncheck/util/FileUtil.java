package com.ctyon.versioncheck.util;

import android.os.Environment;

import java.io.File;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * CreateDate：19-1-16 on 下午5:37
 * Describe:
 * Coder: lee
 */
public class FileUtil {
    /**
     * @return 下载文件夹
     */
    public static File getDownFileDir() {
        File downloadDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);
        if (!downloadDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            downloadDir.mkdirs();
        }
        return downloadDir;
    }
}
