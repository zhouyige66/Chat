package cn.kk20.chat.service;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/6/30 09:48
 * @Version: v1.0
 */
public interface TransactionEventPublishService {

    void publish(String msg);

}
