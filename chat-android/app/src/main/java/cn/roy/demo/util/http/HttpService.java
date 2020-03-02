package cn.roy.demo.util.http;


import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

import io.reactivex.Observer;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/2/12 14:39
 * @Version: v1.0
 */
public interface HttpService {

    void get(String url, Observer<JSONObject> observer);

    void get(String url, @NonNull Map<String, String> headers, Observer<JSONObject> observer);

    void get(String url, @NonNull Map<String, String> headers,
             @NonNull Map<String, Object> queryMap, Observer<JSONObject> observer);

    void getWithoutHeader(String url,@NonNull Map<String, Object> queryMap,
                          Observer<JSONObject> observer);

    void post(String url, Object obj, Observer<JSONObject> observer);

    void post(String url, @NonNull Map<String, String> headers, Observer<JSONObject> observer);

    void post(String url, @NonNull Map<String, String> headers,
              @NonNull Map<String, String> queryMap, Observer<JSONObject> observer);

    void post(String url, @NonNull Map<String, String> headers,
              @NonNull Map<String, String> queryMap, Object obj, Observer<JSONObject> observer);

}
