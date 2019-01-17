package checkversion.ctyon.com.versioncheck.presenter;

import checkversion.ctyon.com.versioncheck.model.VersionRequest;
import checkversion.ctyon.com.versioncheck.model.VersionResponse;

/**
 * CreateDate：19-1-16 on 下午3:52
 * Describe:
 * Coder: lee
 */
public interface VersionCheckerPresenter {

    void checkVersion(VersionRequest versionRequest);

    void download(VersionResponse versionResponse);

}
