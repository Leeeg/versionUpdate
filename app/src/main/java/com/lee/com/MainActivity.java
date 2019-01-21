package com.lee.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lee.versioncheck.VersionChecker;
import com.lee.versioncheck.callback.CheckerCallback;
import com.lee.versioncheck.callback.DownloadCallback;
import com.lee.versioncheck.model.VersionRequest;

public class MainActivity extends AppCompatActivity {

    //    public static final String VERSION_CHECK_URL = "http://47.106.254.237:8089/api/checkVersion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVersionChacker();
        addCallback();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkVersion();
    }

    private void initVersionChacker() {
        String VERSION_CHECK_URL = ""; // your api of checkUodate
        VersionChecker.getInstance().init(this, "ctyon", VERSION_CHECK_URL);
    }

    private void addCallback(){
        VersionChecker.getInstance().setCheckerCallback(new CheckerCallback() {
                    @Override
                    public void onVersionCheckSuccess(boolean installAble, String msg) {
                        if (installAble) {
                            Log.d("Checker_MainActivity", "CheckerCallback : filePath = " + msg);
                        } else {
                            Log.d("Checker_MainActivity", "CheckerCallback : message = " + msg);
                        }
                    }

                    @Override
                    public void onVersionCheckError(String msg) {
                        Log.d("Checker_MainActivity", "CheckerCallback : " + msg);
                    }
                });

        VersionChecker.getInstance().setDownloadCallback(new DownloadCallback() {
            @Override
            public void onPreExecute() {
                Log.d("Checker_MainActivity", "DownloadCallback : onPreExecute ");
            }

            @Override
            public void onPostExecute(int result) {
                Log.d("Checker_MainActivity", "DownloadCallback : result = " + result);
            }

            @Override
            public void onProgressUpdate(int contentLength, int rev, float progress) {
                Log.d("Checker_MainActivity", "DownloadCallback : contentLength = " + contentLength + "  rev = " + rev + "   pro = " + progress);
            }
        });
    }

    private void checkVersion() {

        VersionRequest versionRequest = new VersionRequest();
        versionRequest.setAppName("com.ctyon.intercom");
        versionRequest.setCompanyName("ctyon");
        versionRequest.setDeviceMode("GT11");
        versionRequest.setImei("imei1234");
        versionRequest.setVersionCode("1901121");

        VersionChecker.getInstance().doCheckVersion(versionRequest);

    }



}
