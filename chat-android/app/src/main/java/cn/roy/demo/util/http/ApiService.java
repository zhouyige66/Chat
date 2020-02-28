package cn.roy.demo.util.http;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @Description: 所有接口
 * @Author: Roy
 * @Date: 2019/2/11 18:34
 * @Version: v1.0
 */
public interface ApiService {

    @GET("{url}")
    Observable<JSONObject> get(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<JSONObject> get(@Path(value = "url", encoded = true) String url,
                               @HeaderMap Map<String, String> headers);

    @GET("{url}")
    Observable<JSONObject> get(@Path(value = "url", encoded = true) String url,
                               @HeaderMap Map<String, String> headers,
                               @QueryMap Map<String, String> queryMap);

    @POST("{url}")
    Observable<JSONObject> post(@Path(value = "url", encoded = true) String url,
                                @Body Object object);

    @POST("{url}")
    Observable<JSONObject> post(@Path(value = "url", encoded = true) String url,
                                @HeaderMap Map<String, String> headers);

    @POST("{url}")
    Observable<JSONObject> post(@Path(value = "url", encoded = true) String url,
                                @HeaderMap Map<String, String> headers,
                                @QueryMap Map<String, String> queryMap);

    @POST("{url}")
    Observable<JSONObject> post(@Path(value = "url", encoded = true) String url,
                                @HeaderMap Map<String, String> headers,
                                @QueryMap Map<String, String> queryMap, @Body Object object);

}
