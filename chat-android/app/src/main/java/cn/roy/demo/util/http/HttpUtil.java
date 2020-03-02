package cn.roy.demo.util.http;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/2/12 14:49
 * @Version: v1.0
 */
public class HttpUtil implements HttpService {
    private static HttpUtil instance;

    public static HttpUtil getInstance() {
        if (instance == null) {
            synchronized (HttpUtil.class) {
                instance = new HttpUtil();
            }
        }
        return instance;
    }

    private HttpUtil() {

    }

    private void associate(Observable<JSONObject> observable, Observer<JSONObject> observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                // 拦截数据，进行预处理
                .map(new ServerResponseFunc())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void get(String url, Observer<JSONObject> observer) {
        associate(RetrofitUtil.getInstance().create().get(url), observer);
    }

    @Override
    public void get(String url, @NonNull Map<String, String> headers, Observer<JSONObject> observer) {
        associate(RetrofitUtil.getInstance().create().get(url, headers), observer);
    }

    @Override
    public void get(String url, @NonNull Map<String, String> headers,
                    @NonNull Map<String, Object> queryMap, Observer<JSONObject> observer) {
        associate(RetrofitUtil.getInstance().create().get(url, headers, queryMap), observer);
    }

    @Override
    public void getWithoutHeader(String url, @NonNull Map<String, Object> queryMap, Observer<JSONObject> observer) {
        associate(RetrofitUtil.getInstance().create().getWithoutHeader(url, queryMap), observer);
    }

    @Override
    public void post(String url, Object obj, Observer<JSONObject> observer) {
        associate(RetrofitUtil.getInstance().create().post(url, obj), observer);
    }

    @Override
    public void post(String url, @NonNull Map<String, String> headers, Observer<JSONObject> observer) {
        associate(RetrofitUtil.getInstance().create().post(url, headers), observer);
    }

    @Override
    public void post(String url, @NonNull Map<String, String> headers, @NonNull Map<String, String> queryMap, Observer<JSONObject> observer) {
        associate(RetrofitUtil.getInstance().create().post(url, headers, queryMap), observer);
    }

    @Override
    public void post(String url, @NonNull Map<String, String> headers, @NonNull Map<String, String> queryMap, Object obj, Observer<JSONObject> observer) {
        associate(RetrofitUtil.getInstance().create().post(url, headers, queryMap, obj), observer);
    }

    private static final class ServerResponseFunc implements Function<JSONObject, JSONObject> {
        @Override
        public JSONObject apply(JSONObject jsonObject) throws Exception {
            if (jsonObject.getIntValue("code") == 200) {
                Object obj = jsonObject.get("data");
                if (obj instanceof JSONObject) {
                    return (JSONObject) obj;
                } else {
                    JSONObject data = new JSONObject();
                    data.put("data", obj);
                    return data;
                }
            } else {
                throw new HttpResponseException(jsonObject.getIntValue("code"),
                        jsonObject.getString("msg"));
            }
        }
    }

}
