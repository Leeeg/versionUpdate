package com.ctyon.versioncheck.presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.ctyon.versioncheck.CtyonVersionChecker;
import com.ctyon.versioncheck.model.VersionRequest;
import com.ctyon.versioncheck.model.VersionResponse;
import com.ctyon.versioncheck.util.CheckVersionAsyncTask;
import com.ctyon.versioncheck.util.DownloadAsyncTask;
import com.ctyon.versioncheck.util.GsonInner;

/**
 * CreateDate：19-1-16 on 下午3:53
 * Describe:
 * Coder: lee
 */
@SuppressLint("LongLogTag")
public class VersionCheckerImpl implements VersionCheckerPresenter {

    private static final String TAG = "Checker_VersionCheckerImpl";

    private VersionRequest request;

    private CtyonVersionChecker.Callback callback;

    public VersionCheckerImpl(CtyonVersionChecker.Callback callback) {
        this.callback = callback;
    }

    public interface VersionCallback {
        void success(VersionResponse response);

        void failed(String msg);
    }

    public interface DownloadCallback {
        void success(String filePath);

        void failed(String msg);
    }

    @Override
    public void checkVersion(VersionRequest versionRequest) {
        Log.d(TAG, "checkVersion : " + versionRequest.toString());
        this.request = versionRequest;
        CheckVersionAsyncTask.doExecute(GsonInner.getInstance().toJson(versionRequest), versionCallback);
    }

    @Override
    public void download(VersionResponse versionResponse) {
        DownloadAsyncTask.doExecute(downloadCallback, versionResponse.getVersionCode(), versionResponse.getAppName(), versionResponse.getDownloadUrl());
    }

    private final VersionCallback versionCallback = new VersionCallback() {
        @Override
        public void success(VersionResponse response) {
            try {
                int versionOld = Integer.valueOf(request.getVersionCode());
                int versionNow = Integer.valueOf(response.getVersionCode());
                Log.d(TAG, "checkVersion : versionCallback : success ！  " + "  versionOld = " + versionOld + "  versionNow = " + versionNow);
                if (versionOld < versionNow) {
                    download(response);
                }
            }catch (Exception e){
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

    private final DownloadCallback downloadCallback = new DownloadCallback() {
        @Override
        public void success(String filePath) {
            Log.e(TAG, "checkVersion : downloadCallback : success ! ");
            callback.success(filePath);
        }

        @Override
        public void failed(String msg) {
            Log.e(TAG, "checkVersion : downloadCallback : failed ! ");
            callback.failed(msg);
        }
    };

}
