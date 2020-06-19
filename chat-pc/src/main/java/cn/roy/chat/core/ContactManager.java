package cn.roy.chat.core;

import cn.roy.chat.enity.ContactEntity;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/6/18 10:23
 * @Version: v1.0
 */
public interface ContactManager {
    boolean add(ContactEntity entity);

    boolean remove(ContactEntity entity);

    void clear();

    ContactEntity get(String contactKey);

    boolean update(ContactEntity entity);
}
