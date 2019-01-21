package com.lee.versioncheck.util;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.lee.versioncheck.VersionChecker;
import com.lee.versioncheck.presenter.VersionCheckerImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

/**
 * CreateDate：19-1-14 on 下午2:31
 * Describe:
 * Coder: lee
 */
@SuppressLint("LongLogTag")
public class DownloadAsyncTask extends AsyncTask<String, Integer, Integer> {

    private static final String TAG = "Checker_DownloadAsyncTask";

    private static final String SUFFIX = ".apk";

    private static final int ERROR = 0;
    private static final int SUCCESS = 1;
    private static final int EXIST = 2;

    private File file;
    private RandomAccessFile raFile;

    private String md5;

    private VersionCheckerImpl.DownloadResult downloadResult;

    public static void doExecute(VersionCheckerImpl.DownloadResult downloadResult, String... params) {
        new DownloadAsyncTask(downloadResult).execute(params);
    }

    public DownloadAsyncTask(VersionCheckerImpl.DownloadResult downloadResult) {
        this.downloadResult = downloadResult;
    }

    @Override
    protected Integer doInBackground(String... params) {

        String versionCode = params[0];
        String appName = params[1];
        String downloadUrl = params[2];
        md5 = params[3];

        String fileName = VersionChecker.getInstance().getContain().getBaseName() + "_" +
                appName.replace(".", "_") + "_" + versionCode +
                SUFFIX;

        Log.d(TAG, "checkVersion : doInBackground : versionCode = " + versionCode);
        Log.d(TAG, "checkVersion : doInBackground : appName = " + appName);
        Log.d(TAG, "checkVersion : doInBackground : downloadUrl = " + downloadUrl);
        Log.d(TAG, "checkVersion : doInBackground : fileName = " + fileName);

        file = new File(FileUtil.getDownFileDir(), fileName);

        int offset = CheckerSharePreference.getDownloadIndex();
        int contentLength = 0;
        int total = 0;

        HttpURLConnection connection;
        InputStream inputStream = null;

        try {

            Log.d(TAG, "checkVersion : doInBackground : offset = " + offset);
            URL url = new URL(downloadUrl + "&fileOffset=" + offset);

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(50_000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("content-type", "application/json");

            int resultCode = connection.getResponseCode();
            if (resultCode == 200) {
                Log.d(TAG, "doInBackground : resultCode = " + resultCode);
                contentLength = connection.getContentLength();
                Log.d(TAG, "doInBackground : contentLength = " + contentLength);
                if (file.exists()) {
                    Log.d(TAG, "doInBackground : fileLength = " + file.length());
                    if (offset == file.length() && offset == contentLength) {
                        Log.d(TAG, "doInBackground : the " + fileName + " is existed and valid, go to install");
                        total = offset;
                        return EXIST;
                    } else if (offset != file.length()) {
                        Log.e(TAG, "doInBackground : the " + fileName + " is existed but unvalid, go to delete and createNewFile ");
                        offset = 0;
                        file.delete();
                        file.createNewFile();
                    } else {
                        Log.d(TAG, "doInBackground : the " + fileName + " is existed and valid, go on downloading");
                    }
                } else {
                    Log.d(TAG, "doInBackground : the " + fileName + " is not existed, go to create file ");
                    offset = 0;
                    file.createNewFile();
                }

                raFile = new RandomAccessFile(file, "rw");

                inputStream = connection.getInputStream();
                raFile.seek(offset);
                // 定义缓冲区大小
                byte[] buffer = new byte[1024];
                total = offset;
                int count;
                while ((count = inputStream.read(buffer)) != -1) {
                    total += count;
                    raFile.write(buffer, 0, count);
                    publishProgress(total, contentLength);
                    if (isCancelled()) {
                        throw new IOException();
                    }
                }
            } else {
                Log.e(TAG, "doInBackground : resultCode = " + resultCode);
                return ERROR;
            }
        } catch (IOException e) {
            Log.e(TAG, "doInBackground : 下载异常 " + e);
            if (total > 0 && total == contentLength) {
                return SUCCESS;
            }
            return ERROR;
        } finally {
            try {
                Log.d(TAG, "doInBackground : 结束下载  total = " + total);
                if (total <= contentLength) {
                    CheckerSharePreference.setDownloadIndex(total);
                } else {
                    CheckerSharePreference.setDownloadIndex(0);
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ignored) {
                Log.e(TAG, "doInBackground : inputStream.close() failed ！");
            }
        }
        return SUCCESS;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        Log.d(TAG, "onPostExecute : result = " + result);
        downloadResult.onPostExecute(result);
        if (result == ERROR) {
            downloadResult.failed("download failed !   find the ERROR message by tag : " + TAG);
        } else {
            String mMd5 = FileUtil.getFileMD5(file);
            Log.d(TAG, "onPostExecute : mMd5 = " + mMd5);
            Log.d(TAG, "onPostExecute : md5 = " + md5);
            if (null != mMd5 && !mMd5.isEmpty() && mMd5.equals(md5)){
                downloadResult.success(file.getPath());
            }else {
                file.delete();
                downloadResult.failed("download failed !   the file was corruptive , you can try to checkUpdate again !");
            }

        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        downloadResult.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        float pro = (float) progress[0] / (float) progress[1] * 100;
        Log.d(TAG, "progress= " + progress[0] + ", max= " + progress[1] + "    >>> " + pro + "%");
        downloadResult.onProgressUpdate(progress[1], progress[0], pro);
    }

}
