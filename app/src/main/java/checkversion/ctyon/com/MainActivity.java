package checkversion.ctyon.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import checkversion.ctyon.com.versioncheck.CtyonVersionChecker;
import checkversion.ctyon.com.versioncheck.callback.CheckerCallback;
import checkversion.ctyon.com.versioncheck.model.VersionRequest;

public class MainActivity extends AppCompatActivity implements CheckerCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    @Override
    protected void onResume() {
        super.onResume();

        VersionRequest versionRequest = new VersionRequest();
        versionRequest.setAppName("com.ctyon.intercom");
        versionRequest.setCompanyName("ctyon");
        versionRequest.setDeviceMode("GT11");
        versionRequest.setImei("imei1234");
        versionRequest.setVersionCode("1801121");

        CtyonVersionChecker.getInstance().init(this).setCheckerCallback(this);

        CtyonVersionChecker.getInstance()
                .doCheckVersion(versionRequest);
    }

    @Override
    public void onVersionCheckSuccess(String filePath) {

        Log.d("Checker_MainActivity", "onVersionCheckSuccess : " + filePath);
    }

    @Override
    public void onVersionCheckError(String msg) {

        Log.e("Checker_MainActivity", "onVersionCheckError : " + msg);
    }
}
