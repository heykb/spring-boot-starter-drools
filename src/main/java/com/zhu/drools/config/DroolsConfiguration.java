package com.zhu.drools.config;


import java.util.HashMap;


/**
 * The type Drools configuration.
 * @author heykb
 */
public class DroolsConfiguration {
    private HashMap<String, RepItem> repositories;

    /**
     * Gets repositories.
     *
     * @return the repositories
     */
    public HashMap<String, RepItem> getRepositories() {
        return repositories;
    }

    /**
     * Sets repositories.
     *
     * @param repositories the repositories
     */
    public void setRepositories(HashMap<String, RepItem> repositories) {
        this.repositories = repositories;
    }
}
