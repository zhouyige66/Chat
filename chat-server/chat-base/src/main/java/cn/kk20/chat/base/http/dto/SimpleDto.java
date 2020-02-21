package cn.kk20.chat.base.http.dto;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2019/10/29 13:31
 * @Version: v1.0
 */
public class SimpleDto extends BaseDto{
    private Serializable value;

    public Serializable getValue() {
        return value;
    }

    public SimpleDto setValue(Serializable value) {
        this.value = value;
        return this;
    }

}
