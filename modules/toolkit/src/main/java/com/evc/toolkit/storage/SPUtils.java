package com.evc.toolkit.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Set;


/**
 *****************************************************************************
 * Copyright (C) 2018-2023 HD Corporation. All rights reserved
 * File        : SPUtils.java
 *
 * Description : 操作Shared Preferences公共接口
 * Notes：
 * 1,提供普通数据类型的获取和保存
 * 2，提供缺省文件和指定文件的获取和保存
 * 3，提供数据的清理和文件删除
 * 4，提供对象和List在缺省路径的获取和保存
 *****************************************************************************
 */

public class SPUtils {
    private static final String TAG = "SPUtils";

    private static Context mContext;
    public static void  init(Context context){
        mContext = context;
    }
//    static {
//        mContext = AppTrace.getContext();
//    }

    /**
     * 保存多组给定字符串的值到缺省文件
     * @param map 多组map值，字符串的类型自识别
     * @return 成功返回true，否则 false
     */
    public static boolean put(Map<String, Object> map){
        return put(null, map);
    }

    /**
     * 保存多组给定字符串的值
     * @param filename 文件名称
     * @param map 多组map值，字符串的类型自识别
     * @return 成功返回true，否则 false
     */
    public static boolean put(String filename, Map<String, Object> map) {
        if(null == map || mContext == null){
            return false;
        }

        SharedPreferences.Editor editor = getSp(filename).edit();

        Set<Map.Entry<String, Object>> set = map.entrySet();

        for(Map.Entry<String, Object> entry:set){
            Object value = entry.getValue();
            if (value == null) {
                value = "";
            }
            if (value instanceof String) {
                editor.putString(entry.getKey(), (String) value);
            } else if (value instanceof Integer) {
                editor.putInt(entry.getKey(), (Integer) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(entry.getKey(), (Boolean) value);
            } else if (value instanceof Float) {
                editor.putFloat(entry.getKey(), (Float) value);
            } else if (value instanceof Long) {
                editor.putLong(entry.getKey(), (Long) value);
            }else if(value instanceof Double){
                editor.putLong(entry.getKey(), Double.doubleToLongBits((Double)value));
            } else {
                editor.putString(entry.getKey(), value.toString());
            }
        }
        return editor.commit();
    }

    /**
     * 保存指定字符串的值到缺省文件
     * @param key 字符串名称
     * @param  value 字符串值
     * @return 成功返回true，否则 false
     */
    public static boolean put(String key, Object value) {
        return put(null, key, value);
    }

    /**
     * 保存指定字符串的值
     * @param filename 保存文件名称
     * @param key 字符串名称
     * @param  value 字符串值
     * @return 成功返回true，否则 false
     */
    public static boolean put(String filename, String key, Object value) {
        if(mContext == null){
            return false;
        }

        SharedPreferences.Editor editor = getSp(filename).edit();
        if (value == null) {
            value = "";
        }
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }else if(value instanceof Double){
            editor.putLong(key, Double.doubleToLongBits((Double)value));//Double以Long的形式保存，get的时候需转换
        } else {
            editor.putString(key, value.toString());
        }
        return editor.commit();
    }

    /**
     * 获取缺省文件字符串保存的值
     * @param key 字符串名称
     * @param  defValue 缺省值，类型为int
     * @return 返回字符串保持的值，读取失败返回缺省的
     */
    public static int getInt(String key, int defValue){
        return getInt(null,key,defValue);
    }

    /**
     * 获取指定文件里面，字符串保存的值
     * @param filename 指定文件名称
     * @param key 字符串名称
     * @param  defValue 缺省值，类型为int
     * @return 返回字符串保持的值，读取失败返回缺省的
     */
    public static int getInt(String filename, String key, int defValue){
        if(mContext == null){
            return 0;
        }

        SharedPreferences sp = getSp(filename);

        return sp.getInt(key, defValue);
    }

    /**
     * 获取缺省文件字符串保存的值
     * @param key 字符串名称
     * @param  defValue 缺省值，类型为boolean
     * @return 返回字符串保持的值，读取失败返回缺省的
     */
    public static Boolean getBoolean(String key, Boolean defValue){
        return getBoolean(null,key,defValue);
    }

    /**
     * 获取指定文件里面，字符串保存的值
     * @param filename 指定文件名称
     * @param key 字符串名称
     * @param  defValue 缺省值，类型为boolean
     * @return 返回字符串保持的值，读取失败返回缺省的
     */
    public static Boolean getBoolean(String filename, String key, Boolean defValue){
        if(mContext == null){
            return false;
        }

        SharedPreferences sp = getSp(filename);

        return sp.getBoolean(key, defValue);
    }

    /**
     * 获取缺省文件字符串保存的值
     * @param key 字符串名称
     * @param  defValue 缺省值，类型为float
     * @return 返回字符串保持的值，读取失败返回缺省的
     */
    public static float getFloat(String key, float defValue){
        return getFloat(null,key,defValue);
    }

    /**
     * 获取指定文件里面，字符串保存的值
     * @param filename 指定文件名称
     * @param key 字符串名称
     * @param  defValue 缺省值，类型为float
     * @return 返回字符串保持的值，读取失败返回缺省的
     */
    public static float getFloat(String filename, String key, float defValue){
        if(mContext == null){
            return (float)-1;
        }

        SharedPreferences sp = getSp(filename);

        return sp.getFloat(key, defValue);
    }

    /**
     * 获取缺省文件字符串保存的值
     * @param key 字符串名称
     * @param  defValue 缺省值，类型为long
     * @return 返回字符串保持的值，读取失败返回缺省的
     */
    public static Long getLong(String key, Long defValue){
        return getLong(null,key,defValue);
    }

    /**
     * 获取指定文件里面，字符串保存的值
     * @param filename 指定文件名称
     * @param key 字符串名称
     * @param  defValue 缺省值，类型为long
     * @return 返回字符串保持的值，读取失败返回缺省的
     */
    public static Long getLong(String filename, String key, Long defValue){
        if(mContext == null){
            return (long)-1;
        }

        SharedPreferences sp = getSp(filename);

        return sp.getLong(key, defValue);
    }

    /**
     * 获取缺省文件字符串保存的值
     * @param key 字符串名称
     * @param  defValue 缺省值，类型为string
     * @return 返回字符串保持的值，读取失败返回缺省的
     */
    public static String getString(String key, String defValue){
        return getString(null,key,defValue);
    }

    /**
     * 获取指定文件里面，字符串保存的值
     * @param filename 指定文件名称
     * @param key 字符串名称
     * @param  defValue 缺省值，类型为String
     * @return 返回字符串保持的值，读取失败返回缺省的
     */
    public static String getString(String filename, String key, String defValue){
        if(mContext == null){
            return "";
        }

        SharedPreferences sp = getSp(filename);

        return sp.getString(key, defValue);
    }

    /**
     * 保存对象到缺省文件
     * @param key 对象在文件中的名称
     * @param object 对象实体
     * @return 成功返回true，否则 false
     */
    public static boolean putObject(String key, Object object) {
        if(mContext == null){
            return false;
        }

        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (object == null) {
            SharedPreferences.Editor editor = share.edit().remove(key);
            return editor.commit();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        try {
            baos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = share.edit();
        // 将编码后的字符串写到base64.xml文件中
        editor.putString(key, objectStr);
        return editor.commit();
    }

    /**
     * 获取缺省文件中的对象
     * @param key 对象在文件中的名称
     * @return 成功返回对象，否则为null
     */
    public static Object getObject(String key) {
        if(mContext == null){
            return false;
        }

        SharedPreferences sharePre = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        try {
            String wordBase64 = sharePre.getString(key, "");
            // 将base64格式字符串还原成byte数组
            if (wordBase64.equals("")) { // 不可少，否则在下面会报java.io.StreamCorruptedException
                return null;
            }
            byte[] objBytes = Base64.decode(wordBase64.getBytes(),
                    Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            // 将byte数组转换成product对象
            Object obj = ois.readObject();
            bais.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存List到缺省文件
     * @param key 对象在文件中的名称
     * @param datalist 要保存的List实体
     * @return 成功返回true，否则 false
     *//*
    public static <T> void putDataList(String key, List<T> datalist) {

        if (null == datalist || datalist.size() <= 0)
            return;

        SharedPreferences.Editor editor = getSp(null).edit();
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.putString(key, strJson);
        editor.commit();
    }

    *//**
     * 获取缺省文件中的List
     * @param key List在文件中的名称
     * @return 成功返回List，否则为null
     *//*
    public static <T> List<T> getDataList(String key, Type type) {
        if(null==mContext||null==key) return new ArrayList<T>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        List<T> datalist=new ArrayList<T>();
        String strJson = preferences.getString(key, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, type);
        return datalist;
    }
*/
    /**
     * 移除缺省文件的指定字符串数据
     * @param key 字符串名称
     * @return 成功返回true，否则 false
     */
    public static boolean remove(String key) {
        return remove(null, key);
    }

    /**
     * 移除数据
     * @param filename 指定文件名称
     * @param key 字符串名称
     * @return 成功返回true，否则 false
     */
    public static boolean remove(String filename, String key) {
        if(mContext == null){
            return false;
        }

        SharedPreferences.Editor editor = getSp(filename).edit();
        editor.remove(key);
        return editor.commit();
    }

    /**
     * 清空缺省文件中保存的所有数据
     * @return 成功返回true，否则 false
     */
    public static boolean clear(){
        return clear(null);
    }

    /**
     * 清空指定文件中保存的所有数据
     * @param filename 文件名称
     * @return 成功返回true，否则 false
     */
    public static boolean clear(String filename) {
        if(mContext == null){
            return false;
        }

        SharedPreferences.Editor editor = getSp(filename).edit();
        editor.clear();
        return editor.commit();
    }

    private static SharedPreferences getSp(String file) {
        SharedPreferences sp;

        if(mContext == null){
            return null;
        }

        if(null == file){
            sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        }else{
            sp = mContext.getSharedPreferences(file, Context.MODE_PRIVATE);
        }

        return sp;
    }

}
