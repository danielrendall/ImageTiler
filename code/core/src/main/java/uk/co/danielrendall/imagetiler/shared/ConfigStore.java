package uk.co.danielrendall.imagetiler.shared;

/**
 * @author Daniel Rendall
 */
public interface ConfigStore {
    Integer getInt(String key);

    Integer getInt(String key, int defaultValue);

    Double getDouble(String key);

    Double getDouble(String key, double defaultValue);

    Boolean getBoolean(String key);

    Boolean getBoolean(String key, boolean defaultValue);
}
