package com.ctyon.versioncheck;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ctyon.versioncheck.callback.CheckerCallback;
import com.ctyon.versioncheck.model.VersionRequest;
import com.ctyon.versioncheck.presenter.VersionCheckerImpl;
import com.ctyon.versioncheck.presenter.VersionCheckerPresenter;

/**
 * CreateDate：19-1-14 on 下午2:07
 * Describe:
 * Coder: lee
 */
@SuppressLint("LongLogTag")
public class CtyonVersionChecker {

    private static final String TAG = "Checker_CtyonVersionChecker";

    private VersionCheckerPresenter presenter;

    private Context mContext;

    private CheckerCallback checkerCallback;

    private boolean isChecking;

    public interface Callback {
        void success(String filePath);

        void failed(String msg);
    }

    @Keep
    public static CtyonVersionChecker getInstance() {
        return CtyonVersionCheckerHolder.instance;
    }

    private static class CtyonVersionCheckerHolder {
        private static final CtyonVersionChecker instance = new CtyonVersionChecker();
    }

    private CtyonVersionChecker() {
        Log.e(TAG, "init VersionCheckerPresenter !");
        this.presenter = new VersionCheckerImpl(callback);
    }

    @Keep
    public CtyonVersionChecker init(Context context) {
        if (null == mContext) {
            this.mContext = context;
        }
        return getInstance();
    }

    @Keep
    public void doCheckVersion(VersionRequest versionRequest) {
        if (null == checkerCallback) {
            throw new RuntimeException("please set a valid CheckerCallback !");
        }
        if (null == mContext) {
            throw new RuntimeException("please initialize CtyonVersionChecker first !");
        }
        if (!isChecking) {
            isChecking = true;
            presenter.checkVersion(versionRequest);
        }else {
            Log.d(TAG, "is busy for checking or downloading");
            checkerCallback.onVersionCheckError("the CtyonVersionChecker is busy for checking or downloading");
        }
    }

    @Keep
    public void setCheckerCallback(@NonNull CheckerCallback checkerCallback) {
        this.checkerCallback = checkerCallback;
    }

    public Context getContext() {
        return mContext;
    }

    private final Callback callback = new Callback() {
        @Override
        public void success(String filePath) {
            Log.d(TAG, "check or download apk success : " + filePath);
            checkerCallback.onVersionCheckSuccess(filePath);
            isChecking = false;
        }

        @Override
        public void failed(String msg) {
            Log.e(TAG, "check or download apk failed, callback the error message : " + msg);
            checkerCallback.onVersionCheckError(msg);
            isChecking = false;
        }
    };

}
