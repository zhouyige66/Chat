package cn.kk20.chat.api.controller;

import cn.kk20.chat.model.UserModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2019-01-30 10:49
 * @Version: v1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Test
    public void test() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setId(UUID.randomUUID().toString());
        userModel.setName("roy");
        userModel.setPassword("123456");
        userModel.setPhone("15881016542");
        userModel.setEmail("545344387@qq.com");
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MvcResult result = mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON).content(JSON.toJSONString(userModel)))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MvcResult result = mockMvc.perform(get("/user/login/yige/123456"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testLoginByPost() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","kk20");
        jsonObject.put("password","123456");
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MvcResult result = mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toJSONString()))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}
