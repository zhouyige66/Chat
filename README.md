# Chat
简单实现的即时聊天系统。

**~~重要提示：当前项目正在重构，需要运行查看效果，请使用提交记录为"目录结构调整"以前的代码~~**

**更新：最新版本已经可以查看重构效果**
## 项目简介
- [X] 服务端基于SpringBoot-2.1.2.RELEASE搭建，使用MySQL存储用户信息和即时聊天信息。
- [x] 基于Netty、Websocket、GCDAyncSocket实现四端通信。
- [x] 客户端（Android、IOS）与服务器端通信提供三种方式：
   1. 发送原字符串；
   2. 分隔符方式发送字符串数据；
   3. 自定义协议（自定义编解码器实现）
- [x] 实现Android、IOS心跳检测机制。
- [x] Web端使用了Vue、Vue-Resource框架，直接src引入。
- [x] 客户端均实现登录验证功能。
- [x] <font color="#4590a3">新增PC端代码。
   1. 心跳实现；
   2. 群聊天实现；
   3. 头像显示。</font>
## 系统架构简图
![架构图](https://github.com/zhouyige66/Chat/blob/master/系统架构图.jpg)

## 部署运行
### Server端
 * 安装与配置MySQL数据库，执行chat-server目录下init.sql语句。
 * 修改chat-server中chat-mapper模块下src/main⁩/⁨resources/application-mapper.⁩properties文件中spring.datasource相关的信息。
 * 部署到服务器或直接IDE内运行。
 * <font color="#4590a3">安装与配置redis（用于缓存）</font>
### 客户端
 * 修改相应的host为当前部署的服务器地址，然后分别运行。
## 关于作者
 * Email： 751664206@qq.com
 * 有任何建议或者使用中遇到问题都可以给我发邮件。
