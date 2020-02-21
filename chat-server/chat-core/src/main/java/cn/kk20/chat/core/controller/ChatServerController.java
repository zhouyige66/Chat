package cn.kk20.chat.core.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/2/20 19:37
 * @Version: v1.0
 */
@RestController
@ConditionalOnProperty(prefix = "chat",name = "server-center",matchIfMissing = false)
@RequestMapping("center")
public class ChatServerController {


}
