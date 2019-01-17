package checkversion.ctyon.com.versioncheck.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * CreateDate：19-1-16 on 下午7:03
 * Describe:
 * Coder: lee
 */
public class VersionResponse implements Parcelable {

/**
 * appName : intercom
 * downloadUrl : http://47.97.160.96:8089/cpsFirmware/downloadFirmware?versionNo=1901051&appName=intercom
 * versionCode : 1901051
 * describe : 更新会话层
 */

    /**
     * APP名字
     */
    private String appName;
    /**
     * 下载地址
     */
    private String downloadUrl;
    /**
     * 版本号
     */
    private String versionCode;
    /**
     * 更新信息
     */
    private String describe;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appName);
        dest.writeString(this.downloadUrl);
        dest.writeString(this.versionCode);
        dest.writeString(this.describe);
    }

    public VersionResponse() {
    }

    protected VersionResponse(Parcel in) {
        this.appName = in.readString();
        this.downloadUrl = in.readString();
        this.versionCode = in.readString();
        this.describe = in.readString();
    }

    public static final Parcelable.Creator<VersionResponse> CREATOR = new Parcelable.Creator<VersionResponse>() {
        @Override
        public VersionResponse createFromParcel(Parcel source) {
            return new VersionResponse(source);
        }

        @Override
        public VersionResponse[] newArray(int size) {
            return new VersionResponse[size];
        }
    };

    @Override
    public String toString() {
        return "VersionResponse{" +
                "appName='" + appName + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", describe='" + describe + '\'' +
                '}';
    }
}
