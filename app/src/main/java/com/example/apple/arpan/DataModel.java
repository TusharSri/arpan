package com.example.apple.arpan;

public class DataModel {

    String name;
    String version;

    public DataModel(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public DataModel() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
