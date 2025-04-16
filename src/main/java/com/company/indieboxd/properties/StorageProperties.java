package com.company.indieboxd.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("file")
public class StorageProperties {
    private String storageLocation = "storage";

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }
}