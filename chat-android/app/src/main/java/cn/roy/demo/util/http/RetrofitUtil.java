package cn.roy.demo.util.http;

import com.alibaba.fastjson.support.retrofit.Retrofit2ConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.roy.demo.ApplicationConfig;
import cn.roy.demo.util.LogUtil;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/1/30 16:23
 * @Version: v1.0
 */
public class RetrofitUtil {
    private static volatile RetrofitUtil instance;
    private Retrofit retrofit;

    private RetrofitUtil() {
        initRetrofit();
    }

    public static RetrofitUtil getInstance() {
        if (instance == null) {
            synchronized (RetrofitUtil.class) {
                if (instance == null) {
                    instance = new RetrofitUtil();
                }
            }
        }
        return instance;
    }

    private void initRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(ApplicationConfig.HttpConfig.TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ApplicationConfig.HttpConfig.TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        LogUtil.d(RetrofitUtil.this,"请求方法：" + request.method());
                        LogUtil.d(RetrofitUtil.this,"请求url：" + request.url());
                        Headers headers = request.headers();
                        for (String key : headers.names()) {
                            LogUtil.d(RetrofitUtil.this,"请求header：" + key + ":" + headers.get(key));
                        }
                        RequestBody requestBody = request.body();
                        if (requestBody != null) {
                            Buffer buffer = new Buffer();
                            requestBody.writeTo(buffer);
                            LogUtil.d(RetrofitUtil.this,"请求body：" + buffer.readUtf8());
                        }

                        Response response = chain.proceed(chain.request());
                        ResponseBody responseBody = response.body();
                        String content = responseBody.string();
                        LogUtil.d(RetrofitUtil.this,"返回body：" + content);

                        ResponseBody newResponseBody = ResponseBody.create(responseBody.contentType(),
                                content);
                        return response.newBuilder().body(newResponseBody).build();
                    }
                });
        OkHttpClient okHttpClient = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationConfig.HttpConfig.API_BASE_URL)
                .addConverterFactory(new Retrofit2ConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public ApiService create() {
        return retrofit.create(ApiService.class);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
