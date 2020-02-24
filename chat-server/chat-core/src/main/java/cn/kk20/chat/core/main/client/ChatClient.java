package cn.kk20.chat.core.main.client;

import cn.kk20.chat.core.main.ChatConfigBean;
import cn.kk20.chat.core.initializer.ServerChannelInitializer;
import cn.kk20.chat.core.initializer.WebServerChannelInitializer;
import cn.kk20.chat.core.main.Launcher;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 聊天Server
 * @Author: Roy Z
 * @Date: 2020/2/17 16:00
 * @Version: v1.0
 */
public class ChatClient implements Launcher {
    private final Logger logger = LoggerFactory.getLogger(ChatClient.class);

    // 配置参数
    private ChatConfigBean chatConfigBean;
    // 聊天服务器使用
    private ScheduledExecutorService commonServerExecutor = null, webServerExecutor = null;
    private EventLoopGroup commonServerParentGroup, webServerParentGroup, commonServerChildGroup, webServerChildGroup;
    // 中心服务器使用
    private ScheduledExecutorService centerServerExecutor = null;
    private NioEventLoopGroup nioEventLoopGroup = null;
    private Channel serverChannel = null;
    private boolean connectSuccess = false;

    public void setChatConfigBean(ChatConfigBean chatConfigBean) {
        this.chatConfigBean = chatConfigBean;
    }

    @Override
    public void launch() {
        commonServerExecutor = Executors.newSingleThreadScheduledExecutor();
        webServerExecutor = Executors.newSingleThreadScheduledExecutor();
        centerServerExecutor = Executors.newSingleThreadScheduledExecutor();
        // 通用服务器
        commonServerExecutor.scheduleWithFixedDelay(() -> {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            commonServerParentGroup = new NioEventLoopGroup(1);
            commonServerChildGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
            try {
                serverBootstrap.group(commonServerParentGroup, commonServerChildGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new ServerChannelInitializer(chatConfigBean));
                Channel channel = serverBootstrap.bind(chatConfigBean.getClient().getCommonServer().getPort())
                        .sync().channel();
                channel.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                commonServerParentGroup.shutdownGracefully();
                commonServerChildGroup.shutdownGracefully();
            }
        }, 0, 5, TimeUnit.SECONDS);
        // web服务器
        webServerExecutor.scheduleWithFixedDelay(() -> {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            webServerParentGroup = new NioEventLoopGroup(1);
            webServerChildGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
            try {
                serverBootstrap.group(webServerParentGroup, webServerChildGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new WebServerChannelInitializer());
                Channel channel = serverBootstrap.bind(chatConfigBean.getClient().getWebServer().getPort())
                        .sync().channel();
                channel.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                webServerParentGroup.shutdownGracefully();
                webServerChildGroup.shutdownGracefully();
            }
        }, 0, 5, TimeUnit.SECONDS);
        // 中心服务器
        centerServerExecutor.scheduleWithFixedDelay(() -> {
            nioEventLoopGroup = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ServerChannelInitializer(chatConfigBean, true));
            // 发起异步连接操作
            try {
                ChannelFuture future = bootstrap.connect(chatConfigBean.getClient().getCenter().getHost(),
                        chatConfigBean.getClient().getCenter().getPort()).sync();
                if (future.isSuccess()) {
                    connectSuccess = true;
                    serverChannel = future.channel();
                    logger.debug("连接中心服务器的通道为：{}", serverChannel);
                    // 服务器同步连接断开时,这句代码才会往下执行
                    serverChannel.closeFuture().sync();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                connectSuccess = false;
                nioEventLoopGroup.shutdownGracefully();
            }
        }, 0, chatConfigBean.getClient().getCenter().getAutoRestartTimeInterval(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        if (commonServerParentGroup != null && !commonServerParentGroup.isShutdown()) {
            commonServerParentGroup.shutdownGracefully();
        }
        if (commonServerChildGroup != null && !commonServerChildGroup.isShutdown()) {
            commonServerChildGroup.shutdownGracefully();
        }
        if (webServerParentGroup != null && !webServerParentGroup.isShutdown()) {
            webServerParentGroup.shutdownGracefully();
        }
        if (webServerChildGroup != null && !webServerChildGroup.isShutdown()) {
            webServerChildGroup.shutdownGracefully();
        }
        if (nioEventLoopGroup != null && !nioEventLoopGroup.isShutdown()) {
            nioEventLoopGroup.shutdownGracefully();
        }

        if (commonServerExecutor != null && !commonServerExecutor.isShutdown()) {
            commonServerExecutor.shutdown();
        }
        if (webServerExecutor != null && !webServerExecutor.isShutdown()) {
            webServerExecutor.shutdown();
        }
        if (centerServerExecutor != null && !centerServerExecutor.isShutdown()) {
            centerServerExecutor.shutdown();
        }
    }

    @Override
    public boolean isLaunch() {
        return connectSuccess;
    }

}
