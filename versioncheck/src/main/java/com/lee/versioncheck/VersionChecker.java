package com.lee.versioncheck;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lee.versioncheck.callback.CheckerCallback;
import com.lee.versioncheck.callback.DownloadCallback;
import com.lee.versioncheck.util.Contain;
import com.lee.versioncheck.model.VersionRequest;
import com.lee.versioncheck.presenter.VersionCheckerImpl;
import com.lee.versioncheck.presenter.VersionCheckerPresenter;

/**
 * CreateDate：19-1-14 on 下午2:07
 * Describe:
 * Coder: lee
 */
@SuppressLint("LongLogTag")
public class VersionChecker {

    private static final String TAG = "Checker_VersionCheck";

    private VersionCheckerPresenter presenter;

    private Context mContext;

    private CheckerCallback checkerCallback;

    private boolean isChecking;

    private Contain contain;

    public interface Callback {
        void success(boolean installAble, String filePath);

        void failed(String msg);
    }

    @Keep
    public static VersionChecker getInstance() {
        return VersionCheckerHolder.instance;
    }

    private static class VersionCheckerHolder {
        private static final VersionChecker instance = new VersionChecker();
    }

    private VersionChecker() {
        Log.e(TAG, "init VersionCheckerPresenter !");
        this.presenter = new VersionCheckerImpl(callback);
    }

    @Keep
    public VersionChecker init(Context context, String baseName, String url) {
        if (null == mContext) {
            this.mContext = context;
        }
        if (null == contain){
            setContain(new Contain(baseName, url));
        }
        return getInstance();
    }

    @Keep
    public void doCheckVersion(VersionRequest versionRequest) {
        if (null == checkerCallback) {
            throw new RuntimeException("please set a valid CheckerCallback !");
        }
        if (null == mContext) {
            throw new RuntimeException("please initialize VersionChecker first !");
        }
        if (!isChecking) {
            isChecking = true;
            presenter.checkVersion(versionRequest);
        } else {
            Log.d(TAG, "is busy for checking or downloading");
            checkerCallback.onVersionCheckError("the VersionChecker is busy for checking or downloading");
        }
    }

    private final Callback callback = new Callback() {
        @Override
        public void success(boolean installAble, String filePath) {
            Log.d(TAG, "check or download apk success : " + filePath);
            checkerCallback.onVersionCheckSuccess(installAble, filePath);
            isChecking = false;
        }

        @Override
        public void failed(String msg) {
            Log.e(TAG, "check or download apk failed, callback the error message : " + msg);
            checkerCallback.onVersionCheckError(msg);
            isChecking = false;
        }
    };

    @Keep
    public void setCheckerCallback(@NonNull CheckerCallback checkerCallback) {
        this.checkerCallback = checkerCallback;
    }

    @Keep
    public void setDownloadCallback(DownloadCallback downloadCallback) {
        this.presenter.setDownloadCallback(downloadCallback);
    }

    public Context getContext() {
        return mContext;
    }

    public Contain getContain() {
        return contain;
    }

    private void setContain(Contain contain) {
        this.contain = contain;
    }

}
