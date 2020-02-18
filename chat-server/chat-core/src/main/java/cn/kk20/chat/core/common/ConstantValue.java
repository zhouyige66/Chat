package cn.kk20.chat.core.common;

import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * @Description: 常量
 * @Author: Roy
 * @Date: 2019/1/21 10:46
 * @Version: v1.0
 */
final public class ConstantValue {

    // Message协议头
    public static final int HEAD_DATA = 0x8888;
    // 默认编码格式
    public static final Charset CHARSET = CharsetUtil.UTF_8;
    // 分隔符
    public static final String DELIMITER = "*&v_secretary&*";

    public static final String SERVER_ID = "server_id";

    // redis存储前缀
    public static final String HOST_OF_USER="host_of_user_";
    public static final String MEMBER_OF_GROUP="member_of_group_";
    public static final String FRIEND_OF_USER="friend_of_user_";

}
