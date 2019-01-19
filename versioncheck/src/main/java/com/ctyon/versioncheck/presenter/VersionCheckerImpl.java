package com.ctyon.versioncheck.presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.ctyon.versioncheck.VersionChecker;
import com.ctyon.versioncheck.callback.DownloadCallback;
import com.ctyon.versioncheck.model.VersionRequest;
import com.ctyon.versioncheck.model.VersionResponse;
import com.ctyon.versioncheck.util.CheckVersionAsyncTask;
import com.ctyon.versioncheck.util.DownloadAsyncTask;
import com.ctyon.versioncheck.util.FileUtil;
import com.ctyon.versioncheck.util.GsonInner;

import java.io.File;

/**
 * CreateDate：19-1-16 on 下午3:53
 * Describe:
 * Coder: lee
 */
@SuppressLint("LongLogTag")
public class VersionCheckerImpl implements VersionCheckerPresenter {

    private static final String TAG = "Checker_VersionCheckerImpl";

    private static final String SUFFIX = ".apk";

    private VersionRequest request;

    private VersionChecker.Callback callback;
    private DownloadCallback downloadCallback;

    public VersionCheckerImpl(VersionChecker.Callback callback) {
        this.callback = callback;
    }

    @Override
    public void setDownloadCallback(DownloadCallback downloadCallback) {
        this.downloadCallback = downloadCallback;
    }

    public interface VersionResult {
        void success(VersionResponse response);

        void failed(String msg);
    }

    public interface DownloadResult {
        void success(String filePath);

        void failed(String msg);

        void onPreExecute();

        void onPostExecute(int result);

        void onProgressUpdate(int contentLength, int rev, float progress);
    }

    @Override
    public void checkVersion(VersionRequest versionRequest) {
        Log.d(TAG, "checkVersion : " + versionRequest.toString());
        this.request = versionRequest;
        CheckVersionAsyncTask.doExecute(GsonInner.getInstance().toJson(versionRequest), versionResult);
    }

    @Override
    public void download(VersionResponse versionResponse) {
        DownloadAsyncTask.doExecute(downloadResult, versionResponse.getVersionCode(), versionResponse.getAppName(), versionResponse.getDownloadUrl());
    }

    private final VersionResult versionResult = new VersionResult() {
        @Override
        public void success(VersionResponse response) {
            try {
                int versionOld = Integer.valueOf(request.getVersionCode());
                int versionNow = Integer.valueOf(response.getVersionCode());
                Log.d(TAG, "checkVersion : versionCallback : success ！  " + "  versionOld = " + versionOld + "  versionNow = " + versionNow);
                if (versionOld < versionNow) {
                    Log.d(TAG, "checkVersion : versionCallback :  ------>>>   go to download ! ");
                    download(response);
                } else {
                    String fileName = VersionChecker.getInstance().getContain().getBaseName() +
                            response.getAppName().replace(".", "_") + "_" + response.getVersionCode() +
                            SUFFIX;
                    Log.d(TAG, "checkVersion : versionCallback : ------>>>   try to  delete old file if exit !   fileName = " + fileName);
                    File file = new File(FileUtil.getDownFileDir(), fileName);
                    Log.d(TAG, "checkVersion : versionCallback :  exit = " + file.exists());
                    if (file.exists()) {
                        file.delete();
                        Log.d(TAG, "checkVersion : versionCallback : had deleted ! ");
                    }
                    callback.success(false, " There is not new version !    requestVersion = " + versionOld + "  responseVersion = " + versionNow);
                }
            } catch (Exception e) {
                Log.e(TAG, "checkVersion : versionCallback : failed ! ");
                callback.failed("checkVersion ERROR : " + e);
            }
        }

        @Override
        public void failed(String msg) {
            Log.e(TAG, "checkVersion : versionCallback : failed ! ");
            callback.failed(msg);
        }
    };

    private final DownloadResult downloadResult = new DownloadResult() {
        @Override
        public void success(String filePath) {
            Log.d(TAG, "checkVersion : downloadCallback :   >>>>  success ! <<<< ");
            callback.success(true, filePath);
        }

        @Override
        public void failed(String msg) {
            Log.e(TAG, "checkVersion : downloadCallback : failed ! ");
            callback.failed(msg);
        }

        @Override
        public void onPreExecute() {
            Log.d(TAG, "checkVersion : downloadCallback : onPreExecute ! ");
            if (null != downloadCallback){
                downloadCallback.onPreExecute();
            }
        }

        @Override
        public void onPostExecute(int result) {
            Log.d(TAG, "checkVersion : downloadCallback : onPostExecute ! ");
            if (null != downloadCallback){
                downloadCallback.onPostExecute(result);
            }
        }

        @Override
        public void onProgressUpdate(int contentLength, int rev, float progress) {
            if (null != downloadCallback){
                downloadCallback.onProgressUpdate(contentLength, rev, progress);
            }
        }
    };

}
