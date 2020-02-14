package cn.roy.demo.chat;

import java.util.UUID;

import cn.roy.demo.chat.coder.custom.MessageDecoder;
import cn.roy.demo.chat.coder.custom.MessageEncoder;
import cn.roy.demo.chat.handler.HeartbeatHandler;
import cn.roy.demo.chat.handler.MessageHandler;
import cn.roy.demo.chat.message.ChatMessage;
import cn.roy.demo.chat.message.LoginBody;
import cn.roy.demo.util.CacheManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
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
//    private static final String host = "10.0.2.2";
    private static final String host = "192.168.230.132";

    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private Channel channel;
    private boolean connectSuccess = false;

    // 被观察者
    private Observable<Integer> observable;
    private ObservableEmitter<Integer> observableEmitter;

    private ChatClient() {
        observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                observableEmitter = emitter;
            }
        });
    }

    private void postEvent(int value) {
        // 发送事件
        observableEmitter.onNext(value);
        if(value == 0){
            MessageManager.getInstance().clearUserList();
        }
    }

    private void login(boolean login){
        // 发送断线消息
        ChatMessage<LoginBody> message = new ChatMessage();
        message.setFromUserId(CacheManager.getInstance().getCurrentUserId());
        message.setToUserId("server");
        message.setId(UUID.randomUUID().toString());
        message.setType(ChatMessage.ChatType.LOGIN);
        LoginBody loginBody = new LoginBody();
        loginBody.setUserId(CacheManager.getInstance().getCurrentUserId());
        loginBody.setUserName(CacheManager.getInstance().getCurrentUserName());
        loginBody.setLogin(login);
        message.setBody(loginBody);
        sendMessage(message);
    }

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

    public Observable<Integer> getObservable() {
        return observable;
    }

    public boolean isConnectSuccess() {
        return connectSuccess;
    }

    public void connectServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                group = new NioEventLoopGroup();
                bootstrap = new Bootstrap();
                bootstrap.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline cp = ch.pipeline();
//                            ByteBuf delimiter = Unpooled.copiedBuffer(
//                                    MESSAGE_DELIMITER.getBytes(Charset.forName("UTF-8")));
//                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(
//                                    1024 * 1024, delimiter));
//                            ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
//                            ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
//                            ch.pipeline().addLast(new ShakeHandsHandler());
//                            ch.pipeline().addLast(new HeartBeatHandler());
//                            ch.pipeline().addLast(new ServiceHandler());
                                // 方式一：字符串方式
//                                cp.addLast(
//                                        new StringEncoder(CharsetUtil.UTF_8),
//                                        new StringDecoder(CharsetUtil.UTF_8),
//                                        new IdleStateHandler(5,
//                                                0, 0),
//                                        new HeartbeatHandler(),
//                                        new MessageHandler());
                                // 方式二：分隔符方式
//                                ByteBuf delimiterByteBuf =
//                                        Unpooled.copiedBuffer(ConstantValue.DELIMITER.getBytes());
//                                cp.addLast(
//                                        new DelimiterBasedFrameEncoder(),
//                                        new DelimiterBasedFrameDecoder(2048, delimiterByteBuf),
//                                        new StringDecoder(CharsetUtil.UTF_8),
//                                        new IdleStateHandler(5,
//                                                0, 0),
//                                        new HeartbeatHandler(),
//                                        new MessageHandler());
                                // 方式三：自定义编解码器方式
                                cp.addLast(
                                        new MessageEncoder(),
                                        new MessageDecoder(),
                                        new IdleStateHandler(5, 0,
                                                0),
                                        new HeartbeatHandler(),
                                        new MessageHandler());
                            }
                        });
                // 发起异步连接操作
                try {
                    ChannelFuture future = bootstrap.connect(host, 9999).sync();
                    if (future.isSuccess()) {
                        connectSuccess = true;
                        channel = future.channel();
                        // 发送事件
                        postEvent(1);
                        // 发送登录消息
                        login(true);
                        // 服务器同步连接断开时,这句代码才会往下执行
                        channel.closeFuture().sync();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    connectSuccess = false;
                    group.shutdownGracefully();
                    // 发送事件
                    postEvent(0);
                }
            }
        }).start();
    }

    public void disConnectServer() {
        postEvent(0);
        connectSuccess = false;
        if (group != null && !group.isShutdown()) {
            login(false);
            group.shutdownGracefully();
        }
    }

    public void sendMessage(ChatMessage chatMessage) {
        if (channel != null && channel.isActive()) {
            // 方式一：字符串方式
//            channel.writeAndFlush(JSON.toJSONString(chatMessage));
            // 方式二：分隔符方式或自定义编解码器方式
            channel.writeAndFlush(chatMessage);
        }
    }

}
