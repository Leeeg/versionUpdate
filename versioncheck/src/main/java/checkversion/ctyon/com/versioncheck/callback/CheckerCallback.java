package checkversion.ctyon.com.versioncheck.callback;

/**
 * CreateDate：19-1-16 on 下午4:04
 * Describe:
 * Coder: lee
 */
public interface CheckerCallback {

    void onVersionCheckSuccess(String filePath);

    void onVersionCheckError(String msg);

}
