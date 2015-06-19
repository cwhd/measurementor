package com.nike.mm.business.plugins;

import java.util.Date;

/**
 * Interface for the plugins to use the systems. These plugins will integrate with other systems to get data to feed this system.
 */
public interface IMeasureMentorBusiness {

    /**
     * The type. Must be unique.
     * @return - Name of the plugin.
     */
    String type();

    /**
     * Validates the config. As every object has its own business for configuration this serves to ensure configuration is right
     * before the job starts executing.
     * @param config - The config object to validate.
     * @return - True if valid else false.
     */
    boolean validateConfig(Object config);

    /**
     * Run the system, get the data, put it into ES.
     * @param configInfo - The configuration information that was validated in the validateConfig(String) method.
     */
    void updateData(Object configInfo);
}
