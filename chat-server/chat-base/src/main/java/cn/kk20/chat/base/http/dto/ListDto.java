package cn.kk20.chat.base.http.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2019/10/29 13:31
 * @Version: v1.0
 */
public class ListDto<T extends Serializable> extends BaseDto{
    private List<T> list;

    public ListDto(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
