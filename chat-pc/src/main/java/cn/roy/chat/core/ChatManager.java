package cn.roy.chat.core;

import cn.kk20.chat.base.message.LoginMessage;
import cn.kk20.chat.base.message.Message;
import cn.roy.chat.core.coder.ObjectToStringEncoder;
import cn.roy.chat.core.coder.StringToObjectDecoder;
import cn.roy.chat.core.coder.custom.MessageDecoder;
import cn.roy.chat.core.coder.custom.MessageEncoder;
import cn.roy.chat.core.coder.delimiter.DelimiterBasedFrameEncoder;
import cn.roy.chat.core.handler.HeartbeatHandler;
import cn.roy.chat.core.handler.MessageHandler;
import cn.roy.chat.core.util.LogUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.springframework.util.StringUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/1/14 13:25
 * @Version: v1.0
 */
public class ChatManager {
    private static ChatManager instance;

    private ChatConfig config;
    private ScheduledExecutorService executorService;
    private Bootstrap bootstrap;
    private EventLoopGroup eventLoopGroup;
    private Channel channel;
    // 状态等
    private ChatConnectStatus connectStatus;
    private boolean connectSuccess = false;
    private int failCount = 0;

    public static ChatManager getInstance() {
        if (instance == null) {
            synchronized (ChatManager.class) {
                if (instance == null) {
                    instance = new ChatManager();
                }
            }
        }
        return instance;
    }

    private ChatManager() {

    }

    private void getHostFromServer() {
//        LogUtil.d(this, "调用接口查询聊天服务器Host");
//        Observer<JSONObject> observer = new Observer<JSONObject>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//            }
//
//            @Override
//            public void onNext(JSONObject jsonObject) {
//                failCount = 0;
//                JSONObject map = jsonObject.getJSONObject("map");
//                String host = map.getString("host");
//                if(host.equals("127.0.0.1")){
//                    host = "10.0.2.2";
//                }
//                config.setHost(host);
//                config.setPort(map.getIntValue("port"));
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//            }
//        };
//        Map<String, Object> params = new HashMap<>();
//        params.put("userId", CacheManager.getInstance().getCurrentUserId());
//        HttpUtil.getInstance().getWithoutHeader(ApplicationConfig.HttpConfig.API_GET_NETTY_HOST,
//                params, observer);
    }

    private void reconnect() {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        switch (ConstantValue.CODER_TYPE) {
                            case STRING:// 方式一：字符串方式
                                pipeline.addLast(
                                        new ObjectToStringEncoder(),
                                        new StringEncoder(CharsetUtil.UTF_8),
                                        new StringDecoder(CharsetUtil.UTF_8),
                                        new StringToObjectDecoder());
                                break;
                            case DELIMITER:// 方式二：分隔符方式
                                ByteBuf delimiterByteBuf =
                                        Unpooled.copiedBuffer(ConstantValue.DELIMITER.getBytes());
                                pipeline.addLast(
                                        new DelimiterBasedFrameEncoder(),
                                        new DelimiterBasedFrameDecoder(2048, delimiterByteBuf),
                                        new StringDecoder(CharsetUtil.UTF_8),
                                        new StringToObjectDecoder());
                                break;
                            case CUSTOM:// 方式三：自定义编解码器方式
                                pipeline.addLast(
                                        new MessageEncoder(),
                                        new MessageDecoder(),
                                        new StringToObjectDecoder());
                                break;
                            default:
                                LogUtil.d(this, "暂无实现该种编码方式");
                                break;
                        }
                        pipeline.addLast(new IdleStateHandler(0,
                                        5, 0),
                                new HeartbeatHandler(),
                                new MessageHandler());
                    }
                });
        // 发起异步连接操作
        LogUtil.d(ChatManager.this, "连接server：执行一次");
        try {
            ChannelFuture future = bootstrap.connect(config.getHost(), config.getPort());
            future.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    LogUtil.d(ChatManager.this, "监听回调：" + future.isSuccess());
                    if (future.isSuccess()) {// 连接成功
                        connectSuccess = true;
                        // 重置失败次数
                        failCount = 0;
                        // 登录一次
                        login(true);
                    }
                }
            });
            channel = future.channel();
            LogUtil.d(ChatManager.this, "连接server：等待通道关闭");
            // 服务器同步连接断开时,这句代码才会往下执行
            channel.closeFuture().sync();
            LogUtil.d(ChatManager.this, "连接server：通道正常关闭");
        } catch (InterruptedException e) {
            LogUtil.d(ChatManager.this, "连接server：出现异常");
            e.printStackTrace();
        } finally {
            LogUtil.d(ChatManager.this, "连接server：执行finally");
            connectSuccess = false;
            if (!eventLoopGroup.isShutdown()) {
                eventLoopGroup.shutdownGracefully();
            }
            eventLoopGroup = null;
            failCount++;
            LogUtil.d(ChatManager.this, "失败次数：" + failCount);
        }
    }

    private void login(boolean isLogin) {
        LogUtil.d(this, "执行登录：" + isLogin);
        LoginMessage loginMessage = new LoginMessage();
        loginMessage.setUserId(CacheManager.getInstance().getCurrentUserId());
        loginMessage.setUserName(CacheManager.getInstance().getCurrentUserName());
        loginMessage.setDevice("android");
        loginMessage.setLocation("暂无");
        loginMessage.setLogin(isLogin);
        // 发送消息
        sendMessage(loginMessage);
    }

    private void notifyStatus(ChatConnectStatus status) {
        LogUtil.d(this, "广播状态：" + status.getDes());
        this.status = status;
        if (observableEmitterList.size() == 0) {
            return;
        }
        for (ObservableEmitter emitter : observableEmitterList) {
            if (!emitter.isDisposed()) {
                emitter.onNext(this.status);
            }
        }
    }

    /**
     * 连接聊天服务器
     */
    public void connectServer() {
        if (config == null) {
            LogUtil.d(this, "连接服务器配置未配置");
            return;
        }

        if (connectSuccess) {
            LogUtil.d(this, "已连接服务器，无需重连接");
            return;
        }
        if (executorService != null) {
            executorService.shutdown();
        }
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                /**
                 * 1.判断是否有缓存的服务器地址
                 *  1.1 无服务器地址，则进入第3步
                 *  1.2 缓存了服务器地址，进入第2步
                 * 2.判断重连失败了几次
                 *  2.1 重连次数超过指定次数，进入第3步
                 *  2.2 重连次数未超过指定次数，进入第4步
                 * 3.调用接口获取服务器地址
                 *  3.1 缓存服务器地址，重置重连次数，进入第4步
                 * 4.重连
                 * 5.重登录
                 */
                LogUtil.d(ChatManager.this, "核心线程，重试一次");
                if (StringUtils.isEmpty(config.getHost()) || failCount >= 5) {
                    // 调用接口获取服务器地址
                    getHostFromServer();
                    return;
                }

                reconnect();
            }
        }, 0, config.getAutoReconnectTime(), TimeUnit.SECONDS);
    }

    /**
     * 断开连接
     */
    public void disConnectServer() {
        connectSuccess = false;
        if (eventLoopGroup != null && !eventLoopGroup.isShutdown()) {
            login(false);
            eventLoopGroup.shutdownGracefully();
            eventLoopGroup = null;
        }
    }

    /**
     * 配置连接参数
     * @param config
     */
    public void setConfig(ChatConfig config) {
        this.config = config;
    }

    public ChatConfig getConfig() {
        return config;
    }

    /**
     *
     * @return
     */
    public boolean isConnected() {
        return connectSuccess;
    }

    /**
     * 获取当前连接状态
     * @return
     */
    public ChatConnectStatus getStatus() {
        return connectStatus;
    }

    /**
     * 发送消息
     *
     * @param message
     */
    public void sendMessage(Message message) {
        if (channel != null && channel.isActive()) {
            ChannelFuture channelFuture = channel.writeAndFlush(message);
            String str = String.format("发送消息，类型-%s，发送-%s",
                    message.getMessageType().getDes(),
                    channelFuture.isSuccess() ? "成功" : "失败");
            LogUtil.d(this, str);
        } else {
            LogUtil.d(this, "netty通道不可用");
        }
    }

}
