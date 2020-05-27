package cn.roy.chat.util.http;

import cn.roy.chat.enity.ResultData;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/5/26 10:42
 * @Version: v1.0
 */
public interface HttpRequestTask<Result> {
    ResultData doInBackground();

    void success(String data);

    void fail(String msg);
}
