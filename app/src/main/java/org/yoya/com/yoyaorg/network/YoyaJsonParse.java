package org.yoya.com.yoyaorg.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by yisheng on 2016/1/6.
 */
public class YoyaJsonParse {

    public static <T> T parseGSON(String jsonStr, Class<T> cls) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        T instance = gson.fromJson(jsonStr, cls);
        return instance;
    }


}
