package cn.roy.chat.core;

import cn.roy.chat.enity.ContactEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/6/18 10:25
 * @Version: v1.0
 */
public class ContactManagerImpl implements ContactManager {
    private Map<String, ContactEntity> contactEntityMap = new HashMap<>();

    @Override
    public boolean add(ContactEntity entity) {
        final String key = entity.getIdentifyKey();
        if (contactEntityMap.containsKey(key)) {
            return false;
        }
        contactEntityMap.put(key, entity);
        return true;
    }

    @Override
    public boolean remove(ContactEntity entity) {
        final String key = entity.getIdentifyKey();
        if (contactEntityMap.containsKey(key)) {
            contactEntityMap.remove(key);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        contactEntityMap.clear();
    }

    @Override
    public ContactEntity get(String contactKey) {
        return contactEntityMap.get(contactKey);
    }

    @Override
    public boolean update(ContactEntity entity) {
        final String identifyKey = entity.getIdentifyKey();
        final ContactEntity contactEntity = contactEntityMap.get(identifyKey);
        if (contactEntity != null && contactEntity != entity) {
            contactEntityMap.put(identifyKey, entity);
            return true;
        }
        return false;
    }

}
