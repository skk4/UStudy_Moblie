package org.yoya.com.yoyaorg.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liaozhiwei on 16/5/25.
 */
public class SharedPreferenceUtils {
    public static String USER_INFO = "user_info";
    public static String TOKEN = "token";

    private Context context;

    public SharedPreferenceUtils(Context context) {
        this.context = context;
    }

    /**
     * 写入数据
     */
    public void writeData(String fileName, Map<String, String> data) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        java.util.Iterator it = data.entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
            entry.getKey(); //返回与此项对应的键
            entry.getValue(); //返回与此项对应的值
            editor.putString(entry.getKey() + "", entry.getValue() + "");
        }

        editor.commit();
    }

    /**
     * 写入数据
     */
    public void writeData(String fileName, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(key, value);

        editor.commit();
    }

    /**
     * 读取数据
     *
     * @return Map
     */
    public Map<String, String> readData() {
        Map<String, String> map = new HashMap<String, String>();
        SharedPreferences sp = context.getSharedPreferences("login", Activity.MODE_PRIVATE);
        map.put("name", sp.getString("name", null));
        map.put("pwd", sp.getString("pwd", null));
        map.put("userId", sp.getString("userId", null));
        return map;
    }

    /**
     * 读取数
     *
     * @return String
     */
    public String readData(String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        String value = sp.getString(key, "");
        return value;
    }

    /**
     * 清除数据
     */
    public void destroyData(String str) {
//		SharedPreferences sp = context.getSharedPreferences(str, Context.MODE_PRIVATE);
//		sp.edit().clear().commit();
        context.getSharedPreferences(str, Context.MODE_PRIVATE).edit().clear().commit();
    }

}
