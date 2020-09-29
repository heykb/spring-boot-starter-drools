package com.zhu.drools.config;


import java.util.HashMap;



public class DroolsConfiguration {
    private HashMap<String, RepItem> repositories;

    public HashMap<String, RepItem> getRepositories() {
        return repositories;
    }

    public void setRepositories(HashMap<String, RepItem> repositories) {
        this.repositories = repositories;
    }
}
