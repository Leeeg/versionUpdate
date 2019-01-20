# AppVersionCheck

#### 2019.1.0

#### Description
**library for android app to check update and download the apk file**

#### Installation
project.gradle
```
maven {
            url  "https://dl.bintray.com/leeeg/android"
        }
```
app.gradle
```
implementation 'com.lee.versioncheck:versioncheck:1.0.5'
```

#### API
1.初始化(**必要**，最好使用全局唯一Context且只初始化一次):
> **VersionChecker init(Context context, String baseName, String url)**
>
> baseName ：下载后文件名的开头部分 （eg:baseName_appName_versionCode.apk）
> url:用于检查更新的接口

2.添加检查和下载结果回调：(**必要**，在初始化之后**调用`doCheckVersion`之前**，否则不生效，返回本次检查更新或下载的结果)
> **setCheckerCallback(@NonNull CheckerCallback checkerCallback)**
>回调方法
> onVersionCheckSuccess(boolean installAble, String msg)
installAble = true 表示有新版本且已完成下载完成 msg = apk文件路径
installAble = false 表示没有新版本
> onVersionCheckError(String msg)
检查更新或下载文件出错，具体看msg返回内容

3.添加下载回调：(非必要，在初始化之后**调用`doCheckVersion`之前**，否则不生效,返回下载详细过程)
> **setDownloadCallback(DownloadCallback downloadCallback)**
> 回调方法
> onPreExecute)
下载开始之前
> onProgressUpdate(int contentLength, int rev, float progress)
下载进度  `contentLength = 文件总长度， rev = 已下载的长度， progress = 进度百分比`
> void onPostExecute(int result)
下载结束之后  `result（0：下载失败  1:下载成功   2:文件已存在无需下载）`

4.检查更新：
> **doCheckVersion(VersionRequest versionRequest)**
> ```
> VersionRequest
>     String appName;//APP名/包名(app唯一标识)
>     String versionCode;//APP版本号(eg:1901021)
>     String deviceMode;//机型(eg:GT11)
>     String imei;//设备IMEI/MEID
>     String companyName;//定制名(eg:baifutong)
> ```

**注：`doCheckVersion`之前必须已初始化且添加`CheckerCallback`
否则抛出异常，当前一次检查更新流程未进行完毕时调用`doCheckVersion`会返回`onVersionCheckError`**


#### Instructions (使用实例)

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

```

#### Contribution

1. Fork the repository
2. Create Feat_xxx branch
3. Commit your code
4. Create Pull Request
