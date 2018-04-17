package com.example.demo.helper;

import java.util.HashMap;
import java.util.Map;

public interface MapHelper {

    // use to quickly generate a map with only 1 element
    static Map<String, Object> mapOf(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
