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

    public ConfigStore(String config) {
        doubles = new HashMap<String, Double>();
        ints = new HashMap<String, Integer>();
        if (!"".equals(config)) {
            String[] bits = config.split(",");
            for(String bit: bits) {
                String kvp[] = bit.split("=");
                if (kvp.length != 2) {
                    log.warn(String.format("Bad bit: %s", bit));
                } else {
                    String key = kvp[0].trim().toLowerCase();
                    String value = kvp[1].trim();
                    if (value.indexOf(".") > -1) {
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

    // returns null if no value
    public Double getDouble(String key) {
        return doubles.get(key.toLowerCase());
    }

}
