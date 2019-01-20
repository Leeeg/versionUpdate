# AppVersionCheck

#### Description
{**When you're done, you can delete the content in this README and update the file with details for others getting started with your repository**}

#### Software Architecture
Software architecture description

#### Installation

> implementation 'com.lee.versioncheck:versioncheck:1.0.5'

#### Instructions

```
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
        String VERSION_CHECK_URL = "http://47.97.160.32:8089/api/checkVersion";
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

```

#### Contribution

1. Fork the repository
2. Create Feat_xxx branch
3. Commit your code
4. Create Pull Request
