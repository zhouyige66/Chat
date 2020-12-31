package cn.kk20.chat.api.enums;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/3/16 21:13
 * @Version: v1.0
 */
public enum  ApplyLogTypeEnum {
    ADD_FRIEND(0,"添加好友"),
    ADD_GROUP(1,"添加好友");

    private final Integer type;
    private final String des;

    ApplyLogTypeEnum(Integer type, String des) {
        this.type = type;
        this.des = des;
    }

    public Integer getType() {
        return type;
    }

    public String getDes() {
        return des;
    }

}
