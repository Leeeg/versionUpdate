package com.ctyon.versioncheck.util;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.ctyon.versioncheck.model.Contain;
import com.ctyon.versioncheck.presenter.VersionCheckerImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * CreateDate：19-1-14 on 下午2:31
 * Describe:
 * Coder: lee
 */
@SuppressLint("LongLogTag")
public class DownloadAsyncTask extends AsyncTask<String, Integer, Integer> {

    private static final String TAG = "Checker_DownloadAsyncTask";

    private static final int ERROR = 0;
    private static final int SUCCESS = 1;
    private static final int EXIST = 2;

    private File file;
    private RandomAccessFile raFile;

    private VersionCheckerImpl.DownloadCallback downloadCallback;

    public static void doExecute(VersionCheckerImpl.DownloadCallback downloadCallback, String... params) {
        new DownloadAsyncTask(downloadCallback).execute(params);
    }

    public DownloadAsyncTask(VersionCheckerImpl.DownloadCallback downloadCallback) {
        this.downloadCallback = downloadCallback;
    }

    @Override
    protected Integer doInBackground(String... params) {

        String versionCode = params[0];
        String appName = params[1];
        String downloadUrl = params[2];

        String fileName = Contain.BASE + appName.replace(".", "_") + "_" + versionCode + Contain.SUFFIX;

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
        if (result == ERROR) {
            downloadCallback.failed("download failed !   find the ERROR message by tag : " + TAG);
        } else {
            downloadCallback.success(file.getPath());
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        float pro = (float) progress[0] / (float) progress[1] * 100;
        Log.d(TAG, "max= " + progress[0] + ", progress= " + progress[1] + "    >>> " + pro + "%");
    }

}
