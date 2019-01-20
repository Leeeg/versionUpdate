package com.lee.versioncheck.util;

/**
 * CreateDate：19-1-14 on 下午2:13
 * Describe:
 * Coder: lee
 */
public class Contain {

    private final String url;

    private final String baseName;

    public Contain(String baseName, String url) {
        this.baseName = baseName;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getBaseName() {
        return baseName;
    }
}
