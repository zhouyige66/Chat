package cn.kk20.chat.core.exception;

/**
 * @Description: 消息异常
 * @Author: Roy
 * @Date: 2019-01-28 14:19
 * @Version: v1.0
 */
public class MessageException extends RuntimeException {

    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

}
