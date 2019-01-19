package com.ctyon.versioncheck.util;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.ctyon.versioncheck.VersionChecker;
import com.ctyon.versioncheck.model.Contain;
import com.ctyon.versioncheck.model.Res;
import com.ctyon.versioncheck.model.VersionResponse;
import com.ctyon.versioncheck.presenter.VersionCheckerImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * CreateDate：19-1-14 on 下午2:31
 * Describe:
 * Coder: lee
 */
@SuppressLint("LongLogTag")
public class CheckVersionAsyncTask extends AsyncTask<String, Integer, VersionResponse> {

    private static final String TAG = "Checker_CheckVersionAsyncTask";

    private VersionCheckerImpl.VersionCallback versionCallback;

    public static void doExecute(String param, VersionCheckerImpl.VersionCallback versionCallback) {
        new CheckVersionAsyncTask(versionCallback).execute(param);
    }

    public CheckVersionAsyncTask(VersionCheckerImpl.VersionCallback versionCallback) {
        this.versionCallback = versionCallback;
    }

    @Override
    protected VersionResponse doInBackground(String... strings) {
        try {

            String params = strings[0];
            String downloadPath = VersionChecker.getInstance().getContain().getUrl();
            Log.d(TAG, "checkVersion : doInBackground : path = " + downloadPath);
            Log.d(TAG, "checkVersion : doInBackground : params = " + params);

            //建立连接
            URL url = new URL(downloadPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //设置参数
            connection.setDoOutput(true);     //需要输出
            connection.setDoInput(true);      //需要输入
            connection.setUseCaches(false);   //不允许缓存

            connection.setRequestMethod("POST");      //设置POST方式连接

            //设置请求属性
            connection.setConnectTimeout(10_000);
            connection.setReadTimeout(10_000);
            connection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");

            //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
            connection.connect();

            //建立输入流，向指向的URL传入参数
            OutputStream os = connection.getOutputStream();
            os.write(params.getBytes());
            os.flush();
            os.close();

            //获得响应状态
            int resultCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == resultCode) {
                Log.d(TAG, "checkVersion : doInBackground : resultCode = " + resultCode);
                InputStream is = connection.getInputStream();
                StringBuilder sb = new StringBuilder();
                byte[] bytes = new byte[1024];
                int i = 0;
                while ((i = is.read(bytes)) != -1) {
                    sb.append(new String(bytes, 0, i));
                }
                is.close();
                String result = sb.toString();
                Log.d(TAG, "checkVersion : doInBackground : result = " + result);
                return GsonInner.getInstance().fromJson(result, Res.class).getData();
            }else {
                Log.e(TAG, "checkVersion : doInBackground : resultCode = " + resultCode);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR Message : " + e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR Message : " + e);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR Message : " + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR Message : " + e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(VersionResponse response) {
        super.onPostExecute(response);
        if (null != response){
            Log.d(TAG, "checkVersion : doInBackground : response = " + response.toString());
            versionCallback.success(response);
        }else {
            Log.e(TAG, "checkVersion : doInBackground : the response is NULL ！");
            versionCallback.failed("checkVersion failed !  the response is NULL ！   find the ERROR message by tag : " + TAG);
        }
    }

}
