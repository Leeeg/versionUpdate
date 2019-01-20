package com.lee.versioncheck.callback;

/**
 * CreateDate：19-1-19 on 下午2:00
 * Describe:
 * Coder: lee
 */
public interface DownloadCallback {

    /**
     * 下载开始
     */
    void onPreExecute();

    /**
     * 下载结束（0：下载失败  1:下载成功   2:文件已存在无需下载）
     */
    void onPostExecute(int result);

    /**
     * 下载进度
     */
    void onProgressUpdate(int contentLength, int rev, float progress);

}
