package cn.roy.demo.model;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/2/12 16:55
 * @Version: v1.0
 */
public class User {
    /**
     * password : 123456
     * modifyDate : 2019-01-30T17:06:13.000+0000
     * phone : 13989090909
     * isDelete : true
     * name : kk20
     * id : f52105ea-da07-4366-9305-5be3912adcf9
     * email : 756534264@qq.com
     * createDate : 2019-01-29T17:06:09.000+0000
     */

    private String password;
    private String modifyDate;
    private String phone;
    private boolean isDelete;
    private String name;
    private String id;
    private String email;
    private String createDate;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
