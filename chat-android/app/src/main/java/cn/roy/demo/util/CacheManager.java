package cn.roy.demo.util;

import cn.roy.demo.model.User;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/2/13 13:53
 * @Version: v1.0
 */
public class CacheManager {
    private static CacheManager instance = null;

    private CacheManager() {

    }

    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    /**********功能：缓存用户信息**********/
    private User currentUser;
    private String currentUserId;
    private String currentUserName;

    public void cacheCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        this.currentUserId = currentUser.getId();
        this.currentUserName = currentUser.getName();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

}
