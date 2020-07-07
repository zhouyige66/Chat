package cn.kk20.chat.api.job;

import cn.kk20.chat.api.ConstantValue;
import cn.kk20.chat.dao.model.GroupModel;
import cn.kk20.chat.dao.model.UserModel;
import cn.kk20.chat.api.service.GroupService;
import cn.kk20.chat.api.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/12 09:54
 * @Version: v1.0
 */
@Component
public class SyncUserListJob {
    @Autowired
    UserService userService;
    @Autowired
    GroupService groupService;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Scheduled(initialDelay = 60000, fixedRate = 1000 * 60 * 60)
    public void sync() {
        List<UserModel> userModelList = userService.selectAll();
        for (UserModel userModel : userModelList) {
            String friendList = userModel.getFriendList();
            if (StringUtils.isEmpty(friendList)) {
                continue;
            }

            Set<Long> friendSet = JSON.parseObject(friendList, new TypeReference<Set<Long>>() {
            });
            if (CollectionUtils.isEmpty(friendSet)) {
                continue;
            }
            Long userId = userModel.getId();
            String key = ConstantValue.FRIEND_OF_USER + userId;
            redisTemplate.delete(key);
            for (Long id : friendSet) {
                redisTemplate.opsForSet().add(key, id);
            }
        }
    }

    @Scheduled(initialDelay = 60000, fixedRate = 1000 * 60 * 60)
    public void syncGroupMember() throws Exception {
        List<GroupModel> groupModelList = groupService.selectAll();
        for (GroupModel model : groupModelList) {
            String memberList = model.getMemberList();
            if (StringUtils.isEmpty(memberList)) {
                continue;
            }
            Set<Long> memberSet = JSON.parseObject(memberList, new TypeReference<Set<Long>>() {
            });
            if (CollectionUtils.isEmpty(memberSet)) {
                continue;
            }

            Long groupId = model.getId();
            String key = ConstantValue.MEMBER_OF_GROUP + groupId;
            redisTemplate.delete(key);
            for (Long id : memberSet) {
                redisTemplate.opsForSet().add(key, id);
            }
        }
    }

}
