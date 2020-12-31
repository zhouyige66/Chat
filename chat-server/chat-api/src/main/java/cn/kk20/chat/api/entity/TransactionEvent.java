package cn.kk20.chat.api.entity;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/6/23 15:40
 * @Version: v1.0
 */
public class TransactionEvent {
    private String msg;

    public TransactionEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
