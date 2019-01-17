package checkversion.ctyon.com.versioncheck;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import checkversion.ctyon.com.versioncheck.callback.CheckerCallback;
import checkversion.ctyon.com.versioncheck.model.VersionRequest;
import checkversion.ctyon.com.versioncheck.presenter.VersionCheckerImpl;
import checkversion.ctyon.com.versioncheck.presenter.VersionCheckerPresenter;

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

    public CtyonVersionChecker init(Context context) {
        if (null == mContext) {
            this.mContext = context;

        }
        return getInstance();
    }

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

    public void setCheckerCallback(@NonNull CheckerCallback checkerCallback) {
        this.checkerCallback = checkerCallback;
    }

    public Context getmContext() {
        return mContext;
    }

    private final Callback callback = new Callback() {
        @Override
        public void success(String filePath) {
            Log.d(TAG, "check or download apk success, callback the filePath : " + filePath);
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
