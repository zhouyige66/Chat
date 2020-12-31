package cn.roy.chat.enity;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/5/25 15:33
 * @Version: v1.0
 */
public class UserEntity {
    /**
     * id : 1
     * name : kk20
     * phone : 13982799214
     * email : 751664206@qq.com
     * password :
     * head :
     * isDelete : false
     * createDate : 2020-03-02T16:05:46.000+0800
     * modifyDate : 2020-03-10T15:35:21.000+0800
     * friendList :
     * groupList :
     */

    private int id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String head;
    private boolean isDelete;
    private String createDate;
    private String modifyDate;
    private String friendList;
    private String groupList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public boolean isIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getFriendList() {
        return friendList;
    }

    public void setFriendList(String friendList) {
        this.friendList = friendList;
    }

    public String getGroupList() {
        return groupList;
    }

    public void setGroupList(String groupList) {
        this.groupList = groupList;
    }
}
