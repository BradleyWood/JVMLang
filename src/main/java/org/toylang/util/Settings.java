package org.toylang.util;

import java.util.HashMap;

public class Settings {

    private static final HashMap<String, Object> SETTINGS = new HashMap<>();

    public Object get(final String key) {
        return SETTINGS.get(key);
    }

    public int getInt(final String key) {
        return (int) SETTINGS.get(key);
    }

    public boolean getBoolean(final String key) {
        return (boolean) SETTINGS.get(key);
    }

    public String getString(final String key) {
        return (String) SETTINGS.get(key);
    }

    public void set(String key, Object value) {
        SETTINGS.put(key, value);
    }

}
