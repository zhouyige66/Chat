package cn.roy.demo.chat;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ChatMessageType;
import cn.kk20.chat.base.message.data.LoginData;
import cn.roy.demo.ApplicationConfig;
import cn.roy.demo.chat.coder.ConstantValue;
import cn.roy.demo.chat.coder.custom.MessageDecoder;
import cn.roy.demo.chat.coder.custom.MessageEncoder;
import cn.roy.demo.chat.coder.delimiter.DelimiterBasedFrameEncoder;
import cn.roy.demo.chat.handler.HeartbeatHandler;
import cn.roy.demo.chat.handler.MessageHandler;
import cn.roy.demo.model.ChatServerStatus;
import cn.roy.demo.util.CacheManager;
import cn.roy.demo.util.LogUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
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
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/1/14 13:25
 * @Version: v1.0
 */
public class ChatClient {
    private static ChatClient instance;

    private ChatConfig config;
    private ScheduledExecutorService executorService;
    private Bootstrap bootstrap;
    private EventLoopGroup eventLoopGroup;
    private Channel channel;
    private boolean connectSuccess = false;
    private int failCount = 0;
    // 被观察者
    private Observable<ChatServerStatus> observable;
    private List<ObservableEmitter<ChatServerStatus>> observableEmitterList;
    private ChatServerStatus status;

    public static ChatClient getInstance() {
        if (instance == null) {
            synchronized (ChatClient.class) {
                if (instance == null) {
                    instance = new ChatClient();
                }
            }
        }
        return instance;
    }

    private ChatClient() {
        observableEmitterList = new ArrayList<>();
        observable = Observable.create(new ObservableOnSubscribe<ChatServerStatus>() {
            @Override
            public void subscribe(ObservableEmitter<ChatServerStatus> emitter) throws Exception {
                observableEmitterList.add(emitter);
            }
        });
        status = ChatServerStatus.INIT;
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
        notifyStatus(ChatServerStatus.CONNECTING);
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
                if (TextUtils.isEmpty(config.getHost()) || failCount >= 5) {
                    // 调用接口获取服务器地址
                    getHostFromServer();
                    return;
                }

                reconnect();
            }
        }, 0, config.getAutoReconnectTime(), TimeUnit.SECONDS);
    }

    private void getHostFromServer() {
        LogUtil.d(this, "调用接口查询聊天服务器Host");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        failCount = 0;
        config.setHost(ApplicationConfig.NettyConfig.CHAT_SERVER_HOST);
        reconnect();
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
                            case 0:// 方式一：字符串方式
                                pipeline.addLast(
                                        new StringEncoder(CharsetUtil.UTF_8),
                                        new StringDecoder(CharsetUtil.UTF_8));
                                break;
                            case 1:// 方式二：分隔符方式
                                ByteBuf delimiterByteBuf =
                                        Unpooled.copiedBuffer(ConstantValue.DELIMITER.getBytes());
                                pipeline.addLast(
                                        new DelimiterBasedFrameEncoder(),
                                        new DelimiterBasedFrameDecoder(2048, delimiterByteBuf),
                                        new StringDecoder(CharsetUtil.UTF_8));
                                break;
                            case 2:// 方式三：自定义编解码器方式
                                pipeline.addLast(
                                        new MessageEncoder(),
                                        new MessageDecoder());
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
        LogUtil.d(ChatClient.this, "连接server：执行一次");
        try {
            ChannelFuture future = bootstrap.connect(config.getHost(), config.getPort());
            future.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    LogUtil.d(ChatClient.this, "监听回调：" + future.isSuccess() + "/" + future.isDone());
                    if (future.isSuccess()) {// 连接成功
                        connectSuccess = true;
                        // 重置失败次数
                        failCount = 0;
                        // 登录一次
                        login(true);
                        notifyStatus(ChatServerStatus.CONNECTED);
                    }
                }
            });
            channel = future.channel();
            LogUtil.d(ChatClient.this, "连接server：等待通道关闭");
            // 服务器同步连接断开时,这句代码才会往下执行
            channel.closeFuture().sync();
            notifyStatus(ChatServerStatus.INIT);
        } catch (InterruptedException e) {
            LogUtil.d(ChatClient.this, "连接server：出现异常");
            e.printStackTrace();
            notifyStatus(ChatServerStatus.SERVER_ERROR);
        } finally {
            LogUtil.d(ChatClient.this, "连接server：执行finally");
            connectSuccess = false;
            if (!eventLoopGroup.isShutdown()) {
                eventLoopGroup.shutdownGracefully();
            }
            eventLoopGroup = null;
            failCount++;
            LogUtil.d(ChatClient.this, "失败次数：" + failCount);
        }
    }

    private void login(boolean isLogin) {
        LogUtil.d(this, "执行登录：" + isLogin);
        LoginData loginData = new LoginData();
        loginData.setLogin(isLogin);
        loginData.setUserId(CacheManager.getInstance().getCurrentUserId());
        loginData.setUserName(CacheManager.getInstance().getCurrentUserName());

        ChatMessage message = new ChatMessage();
        message.setMessageType(ChatMessageType.LOGIN);
        message.setFromUserId(CacheManager.getInstance().getCurrentUserId());
        message.setToUserId(ConstantValue.SERVER_ID);
        message.setBodyData(loginData);
        // 发送消息
        sendMessage(message);
    }

    private void notifyStatus(ChatServerStatus status) {
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

    public void disConnectServer() {
        connectSuccess = false;
        if (eventLoopGroup != null && !eventLoopGroup.isShutdown()) {
            login(false);
            eventLoopGroup.shutdownGracefully();
            eventLoopGroup = null;
        }
    }

    public void setConfig(ChatConfig config) {
        this.config = config;
    }

    public ChatConfig getConfig() {
        return config;
    }

    public boolean isConnectSuccess() {
        return connectSuccess;
    }

    public Observable<ChatServerStatus> getObservable() {
        return observable;
    }

    public ChatServerStatus getStatus() {
        return status;
    }

    /**
     * 发送消息
     *
     * @param chatMessage
     */
    public void sendMessage(ChatMessage chatMessage) {
        if (channel != null && channel.isActive()) {
            ChannelFuture channelFuture;
            if (ConstantValue.CODER_TYPE == 0) {
                // 方式一：字符串方式
                channelFuture = channel.writeAndFlush(JSON.toJSONString(chatMessage));
            } else {
                // 方式二：分隔符方式或自定义编解码器方式
                channelFuture = channel.writeAndFlush(chatMessage);
            }

            String str = String.format("发送消息，类型-%s，发送-%s",
                    chatMessage.getMessageType().getDes(),
                    channelFuture.isSuccess() ? "成功" : "失败");
            LogUtil.d(this, str);
        } else {
            LogUtil.d(this, "netty通道不可用");
        }
    }

}
