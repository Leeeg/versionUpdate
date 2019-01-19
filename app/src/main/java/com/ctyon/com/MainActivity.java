package com.ctyon.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ctyon.versioncheck.VersionChecker;
import com.ctyon.versioncheck.callback.CheckerCallback;
import com.ctyon.versioncheck.model.VersionRequest;

public class MainActivity extends AppCompatActivity {

    //    public static final String VERSION_CHECK_URL = "http://47.106.254.237:8089/api/checkVersion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    @Override
    protected void onResume() {
        super.onResume();


        String VERSION_CHECK_URL = "http://192.168.0.16:8089/api/checkVersion";

        VersionRequest versionRequest = new VersionRequest();
        versionRequest.setAppName("com.ctyon.intercom");
        versionRequest.setCompanyName("ctyon");
        versionRequest.setDeviceMode("GT11");
        versionRequest.setImei("imei1234");
        versionRequest.setVersionCode("105");

        VersionChecker.getInstance()
                .init(this, "ctyon", VERSION_CHECK_URL)
                .setCheckerCallback(new CheckerCallback() {
                    @Override
                    public void onVersionCheckSuccess(boolean installAble, String msg) {
                        if (installAble) {
                            Log.d("Checker_MainActivity", "onVersionCheckSuccess : filePath = " + msg);
                        } else {
                            Log.e("Checker_MainActivity", "onVersionCheckSuccess : message = " + msg);
                        }
                    }

                    @Override
                    public void onVersionCheckError(String msg) {

                        Log.e("Checker_MainActivity", "onVersionCheckError : " + msg);
                    }
                });

        VersionChecker.getInstance().doCheckVersion(versionRequest);
    }

}
