package edu.ufl.cnt5106c.exceptions;

/**
 * Created by sayak on 10/27/17.
 */
public class ConfigException extends Exception {
    private String configFileName;
    public ConfigException(String configFileName) {
        this.configFileName = configFileName;
    }

    public String getConfigFileName() {
        return configFileName;
    }

    @Override
    public String toString() {
        return "Unknown configuration option in config file " + configFileName;
    }
}
