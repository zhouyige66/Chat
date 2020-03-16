package cn.kk20.chat.api.controller;

import cn.kk20.chat.api.ConstantValue;
import cn.kk20.chat.api.call.CallCenterServerService;
import cn.kk20.chat.base.http.ResultData;
import cn.kk20.chat.base.http.dto.MapDto;
import cn.kk20.chat.base.message.ChatMessage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @Description: netty相关的控制器
 * @Author: Roy Z
 * @Date: 2020/3/16 09:20
 * @Version: v1.0
 */
@RestController
@RequestMapping("netty")
@Api(tags = "Netty相关Controller")
public class NettyRelateController {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    CallCenterServerService callCenterServerService;

    @GetMapping("getHost")
    @ApiOperation(value = "查询netty服务主机", notes = "功能：查询负载均衡netty服务主机")
    @ApiImplicitParam(name = "userId", value = "用户ID")
    public ResultData addFriend(@RequestParam Long userId) throws Exception {
        if (userId == null) {
            return ResultData.requestError();
        }

        // 查询当前可提供服务的主机
        String str = (String) redisTemplate.opsForValue().get(ConstantValue.LIST_OF_SERVER);
        Set<String> hostSet = JSON.parseObject(str, new TypeReference<Set<String>>() {
        });
        if (CollectionUtils.isEmpty(hostSet)) {
            return ResultData.fail(ResultData.ResultCode.CUSTOM_ERROR, "无主机可用");
        }
        // 查询单个主机连接数量，做平均负载均衡
        String minConnectHost = null;
        int minConnectCount = 0;
        for (String host : hostSet) {
            Object object = redisTemplate.opsForValue().get(ConstantValue.STATISTIC_OF_HOST + host);
            if (object == null) {
                minConnectHost = host;
                break;
            } else {
                int count = (int) object;
                if (minConnectHost == null || count < minConnectCount) {
                    minConnectHost = host;
                    minConnectCount = count;
                }
            }
        }

        String[] split = minConnectHost.split(":");
        String[] ports = split[1].split("&");
        String host = split[0];
        int port = Integer.valueOf(ports[0]);
        MapDto mapDto = new MapDto();
        mapDto.add("host", host);
        mapDto.add("port", port);
        return ResultData.success(mapDto);
    }

    @PostMapping("sendMessage")
    @ApiOperation(value = "发送消息", notes = "功能：netty无连接时，发送消息")
    @ApiImplicitParam(name = "chatMessage", value = "消息", dataType = "ChatMessage")
    public ResultData sendMessage(@RequestBody ChatMessage chatMessage) throws Exception {
        if (chatMessage == null || chatMessage.getToUserId() == null) {
            return ResultData.requestError();
        }

        ResultData resultData = callCenterServerService.sendChatMessage(chatMessage);
        return resultData;
    }

}
