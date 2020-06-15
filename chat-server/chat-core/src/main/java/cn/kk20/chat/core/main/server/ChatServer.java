package cn.kk20.chat.core.main.server;

import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.config.ChatConfigBean;
import cn.kk20.chat.core.main.Launcher;
import cn.kk20.chat.core.main.ServerComponent;
import cn.kk20.chat.core.main.server.initializer.ServerChannelInitializer;
import cn.kk20.chat.core.util.RedisUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 聊天服务器中心Server
 * @Author: Roy Z
 * @Date: 2020/2/17 16:00
 * @Version: v1.0
 */
@ServerComponent
public class ChatServer implements Launcher {
    private ScheduledExecutorService serverExecutor = null;
    private NioEventLoopGroup serverParentGroup = null, serverChildGroup = null;
    private boolean launch = false;

    @Autowired
    ChatConfigBean chatConfigBean;
    @Autowired
    ServerChannelInitializer serverChannelInitializer;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public void launch() {
        serverExecutor = Executors.newSingleThreadScheduledExecutor();
        serverExecutor.scheduleWithFixedDelay(() -> {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverParentGroup = new NioEventLoopGroup(1);
            serverChildGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
            try {
                serverBootstrap.group(serverParentGroup, serverChildGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(serverChannelInitializer);
                // Start the client.
                ChannelFuture channelFuture = serverBootstrap.bind(chatConfigBean.getServer().getPort()).sync();
                if (channelFuture.isSuccess()) {
                    launch = true;
                }
                Channel channel = channelFuture.channel();
                // Wait until the connection is closed.
                channel.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                serverParentGroup.shutdownGracefully();
                serverChildGroup.shutdownGracefully();
                launch = false;
            }
        }, 0, chatConfigBean.getServer().getAutoRestartTimeInterval(), TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        if (serverParentGroup != null && !serverParentGroup.isShutdown()) {
            serverParentGroup.shutdownGracefully();
        }
        if (serverChildGroup != null && !serverChildGroup.isShutdown()) {
            serverChildGroup.shutdownGracefully();
        }
        if (serverExecutor != null && !serverExecutor.isShutdown()) {
            serverExecutor.shutdown();
        }
        // 回收操作
        redisUtil.delete(ConstantValue.LIST_OF_SERVER);
    }

    @Override
    public boolean isLaunch() {
        return launch;
    }

}
