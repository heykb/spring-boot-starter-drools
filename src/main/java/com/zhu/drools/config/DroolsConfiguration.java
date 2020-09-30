package com.zhu.drools.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;


/**
 * The type Drools configuration.
 *
 * @author heykb
 */
public class DroolsConfiguration {
    private HashMap<String, RepItem> repositories;
    private Long scannerInterval;
    private Boolean scannerEnable = false;

    /**
     * Gets scanner interval.
     *
     * @return the scanner interval
     */
    public Long getScannerInterval() {
        return scannerInterval;
    }

    /**
     * Sets scanner interval.
     *
     * @param scannerInterval the scanner interval
     */
    public void setScannerInterval(Long scannerInterval) {
        this.scannerInterval = scannerInterval;
    }

    /**
     * Gets scanner enable.
     *
     * @return the scanner enable
     */
    public Boolean getScannerEnable() {
        return scannerEnable;
    }

    /**
     * Sets scanner enable.
     *
     * @param scannerEnable the scanner enable
     */
    public void setScannerEnable(Boolean scannerEnable) {
        this.scannerEnable = scannerEnable;
    }

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
