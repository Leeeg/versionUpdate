package com.ctyon.versioncheck.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * CreateDate：19-1-16 on 下午4:06
 * Describe:
 * Coder: lee
 */
public class VersionRequest implements Parcelable {

    /**
     * APP名字（eg:Intercom）
     */
    private String appName;

    /**
     * APP版本号(eg:1901021)
     */
    private String versionCode;

    /**
     * 机型(eg:GT11)
     */
    private String deviceMode;

    /**
     * 设备IMEI
     */
    private String imei;

    /**
     * 定制公司名（eg:baifutong）
     */
    private String companyName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getDeviceMode() {
        return deviceMode;
    }

    public void setDeviceMode(String deviceMode) {
        this.deviceMode = deviceMode;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "VersionBean{" +
                "appName=" + appName +
                ", versionCode='" + versionCode + '\'' +
                ", deviceMode='" + deviceMode + '\'' +
                ", imei='" + imei + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appName);
        dest.writeString(this.versionCode);
        dest.writeString(this.deviceMode);
        dest.writeString(this.imei);
        dest.writeString(this.companyName);
    }

    public VersionRequest() {
    }

    protected VersionRequest(Parcel in) {
        this.appName = in.readString();
        this.versionCode = in.readString();
        this.deviceMode = in.readString();
        this.imei = in.readString();
        this.companyName = in.readString();
    }

    public static final Creator<VersionRequest> CREATOR = new Creator<VersionRequest>() {
        @Override
        public VersionRequest createFromParcel(Parcel source) {
            return new VersionRequest(source);
        }

        @Override
        public VersionRequest[] newArray(int size) {
            return new VersionRequest[size];
        }
    };
}
