package com.ctyon.versioncheck.presenter;

import com.ctyon.versioncheck.model.VersionRequest;
import com.ctyon.versioncheck.model.VersionResponse;

/**
 * CreateDate：19-1-16 on 下午3:52
 * Describe:
 * Coder: lee
 */
public interface VersionCheckerPresenter {

    void checkVersion(VersionRequest versionRequest);

    void download(VersionResponse versionResponse);

}
