package uk.co.danielrendall.imagetiler.shared;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 28-Mar-2010
 * Time: 10:02:08
 * To change this template use File | Settings | File Templates.
 */
public class ConfigStore {
    public final static Logger log = Logger.getLogger(ConfigStore.class);
    private final Map<String, Double> doubles;
    private final Map<String, Integer> ints;
    private final Map<String, Boolean> bools;

    // rather a lot happening in this constructor - could use
    // a builder and pass some UnmodifiableMaps to this
    public ConfigStore(String config) {
        doubles = new HashMap<String, Double>();
        ints = new HashMap<String, Integer>();
        bools = new HashMap<String, Boolean>();
        if (!"".equals(config)) {
            String[] bits = config.split(",");
            for(String bit: bits) {
                String kvp[] = bit.split("=");
                if (kvp.length != 2) {
                    log.warn(String.format("Bad bit: %s", bit));
                } else {
                    String key = kvp[0].trim().toLowerCase();
                    String value = kvp[1].trim();
                    if (Boolean.toString(true).equalsIgnoreCase(value)) {
                        bools.put(key, true);
                    } else if (Boolean.toString(false).equalsIgnoreCase(value)) {
                        bools.put(key, false);
                    } else if (value.indexOf(".") > -1) {
                        try {
                            double d = Double.parseDouble(value);
                            doubles.put(key, d);
                        } catch (NumberFormatException e) {
                            log.warn(String.format("Bad double '%s' for key %s", value, key));
                        }
                    } else {
                        try {
                            int i = Integer.parseInt(value);
                            ints.put(key, i);
                        } catch (NumberFormatException e) {
                            log.warn(String.format("Bad int '%s' for key %s", value, key));
                        }
                    }

                }
            }
        }
    }

    // returns null if no value
    public Integer getInt(String key) {
        return ints.get(key.toLowerCase());
    }

    public Integer getInt(String key, int defaultValue) {
        Integer i = getInt(key);
        return (i != null) ? i : defaultValue;
    }

    // returns null if no value
    public Double getDouble(String key) {
        Double d = doubles.get(key.toLowerCase());
        if (d != null) return d;
        Integer i = getInt(key);
        if (i != null) return (double) i;
        return null;
    }

    public Double getDouble(String key, double defaultValue) {
        Double d = getDouble(key);
        return (d != null) ? d : defaultValue;
    }

    // returns null if no value
    public Boolean getBoolean(String key) {
        return bools.containsKey(key) ? bools.get(key.toLowerCase()) : null;
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        Boolean b = getBoolean(key);
        return (b != null) ? b : defaultValue;
    }
}
