package cn.kk20.chat.core.main.server;

import cn.kk20.chat.core.common.ChatConfigBean;
import cn.kk20.chat.core.initializer.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 聊天服务器中心Server
 * @Author: Roy Z
 * @Date: 2020/2/17 16:00
 * @Version: v1.0
 */
@Component
public final class ChatServer {

    @Autowired
    ChatConfigBean chatConfigBean;

    // 其他属性
    private ScheduledExecutorService executorService = null;
    private NioEventLoopGroup chatServerParentGroup = null, chatServerChildGroup = null;
    private boolean launch = false;

    public void start() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(() -> {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            chatServerParentGroup = new NioEventLoopGroup(1);
            chatServerChildGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
            try {
                serverBootstrap.group(chatServerParentGroup, chatServerChildGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new ServerChannelInitializer(chatConfigBean.getCoderType()));
                ChannelFuture channelFuture = serverBootstrap.bind(chatConfigBean.getServer().getPort()).sync();
                if (channelFuture.isSuccess()) {
                    launch = true;
                }
                Channel channel = channelFuture.channel();
                channel.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                chatServerParentGroup.shutdownGracefully();
                chatServerChildGroup.shutdownGracefully();
                launch = false;
            }
        }, 0, chatConfigBean.getServer().getAutoRestartTimeInterval(), TimeUnit.SECONDS);
    }

    public void stop() {
        if (chatServerParentGroup != null && !chatServerParentGroup.isShutdown()) {
            chatServerParentGroup.shutdownGracefully();
        }
        if (chatServerChildGroup != null && !chatServerChildGroup.isShutdown()) {
            chatServerChildGroup.shutdownGracefully();
        }
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    public boolean isLaunch() {
        return launch;
    }

}
