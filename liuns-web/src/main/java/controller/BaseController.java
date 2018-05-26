package controller;

import com.google.common.collect.Maps;

import java.util.Map;

public class BaseController {

    protected Object success(Object obj) {
        Map<String, Object> map = Maps.newConcurrentMap();
        map.put("code", 200);
        map.put("data", obj);
        return map;
    }

    protected Object fail(String msg) {
        Map<String, Object> map = Maps.newConcurrentMap();
        map.put("code", 500);
        map.put("msg", msg);
        return map;
    }

    protected Object fail(int code, String msg) {
        Map<String, Object> map = Maps.newConcurrentMap();
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }
}
