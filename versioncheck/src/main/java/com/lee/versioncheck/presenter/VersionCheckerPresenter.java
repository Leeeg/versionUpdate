package com.lee.versioncheck.presenter;

import com.lee.versioncheck.callback.DownloadCallback;
import com.lee.versioncheck.model.VersionRequest;
import com.lee.versioncheck.model.VersionResponse;

/**
 * CreateDate：19-1-16 on 下午3:52
 * Describe:
 * Coder: lee
 */
public interface VersionCheckerPresenter {

    void checkVersion(VersionRequest versionRequest);

    void download(VersionResponse versionResponse);

    void setDownloadCallback(DownloadCallback downloadCallback);

}
