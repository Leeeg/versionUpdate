package com.ctyon.versioncheck.model;

/**
 * CreateDate：19-1-17 on 下午2:42
 * Describe:
 * Coder: lee
 */
public class Res {

    /**
     * rtnCode : 200
     * msg : success
     * data : {"appName":"intercom","downloadUrl":"http://47.97.160.96:8089/cpsFirmware/downloadFirmware?versionNo=1901051&appName=intercom","versionCode":"1901051","describe":"更新会话层"}
     */

    private int rtnCode;
    private String msg;
    private VersionResponse data;

    public int getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(int rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public VersionResponse getData() {
        return data;
    }

    public void setData(VersionResponse data) {
        this.data = data;
    }

}
