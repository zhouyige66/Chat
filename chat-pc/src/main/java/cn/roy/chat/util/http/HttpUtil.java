package cn.roy.chat.util.http;

import cn.roy.chat.enity.ResultData;
import com.alibaba.fastjson.JSON;
import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/5/26 10:41
 * @Version: v1.0
 */
public class HttpUtil {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private HttpUtil() {

    }

    public static <Data> void execute(HttpRequestTask callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final ResultData resultData = callback.doInBackground();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (resultData.getCode() == 200) {
                                callback.success(JSON.toJSONString(resultData.getData()));
                            } else {
                                callback.fail(resultData.getMsg());
                            }
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            callback.fail(e.getMessage());
                        }
                    });
                }
            }
        });
    }

}
