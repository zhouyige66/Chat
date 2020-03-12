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

    public static final Long SERVER_ID = 0L;

    /**********功能：redis存储前缀**********/
    // 通用前缀
    public static final String HOST_OF_USER = "host_of_user:";// 用户连接的主机地址
    public static final String MEMBER_OF_GROUP = "member_of_group:";// 群成员
    public static final String FRIEND_OF_USER = "friend_of_user:";// 好友列表
    public static final String STATISTIC_OF_HOST = "statistic_of_host:";// 指定主机已连接数量
    // 中心主机维护
    public static final String LIST_OF_HOST = "list_of_host";// 当前可服务主机列表

}
