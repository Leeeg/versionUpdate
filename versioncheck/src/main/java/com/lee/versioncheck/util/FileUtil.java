package com.lee.versioncheck.util;

import android.os.Environment;
import android.support.annotation.Keep;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * CreateDate：19-1-16 on 下午5:37
 * Describe:
 * Coder: lee
 */
@Keep
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

    /**
     * 获取文件MD5
     *
     * @param file
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getFileMD5(File file) {
        try {
            if (!file.isFile()) {
                return null;
            }
            MessageDigest digest;
            FileInputStream in;
            byte buffer[] = new byte[1024];
            int len;
            digest = MessageDigest.getInstance("MD5");

            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
