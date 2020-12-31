package cn.kk20.chat.api.service;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/6/30 09:48
 * @Version: v1.0
 */
public interface TransactionEventPublishService {

    void publish(String msg);

}
