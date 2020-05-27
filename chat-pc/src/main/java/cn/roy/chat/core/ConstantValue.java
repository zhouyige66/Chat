package cn.roy.chat.core;

import cn.roy.chat.core.coder.CoderType;
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
    // 编码类型
    public static final CoderType CODER_TYPE = CoderType.CUSTOM;
    // 服务器ID
    public static final Long SERVER_ID = 0L;
}
