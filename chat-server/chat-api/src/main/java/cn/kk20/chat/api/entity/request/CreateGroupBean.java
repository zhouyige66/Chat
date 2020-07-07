package cn.kk20.chat.api.entity.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/5 14:28
 * @Version: v1.0
 */
public class CreateGroupBean {
    @ApiModelProperty(value ="群名称")
    private String name;
    @ApiModelProperty(value ="群简述")
    private String description;
    @ApiModelProperty(value ="创建人")
    private Long creator;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }
}
