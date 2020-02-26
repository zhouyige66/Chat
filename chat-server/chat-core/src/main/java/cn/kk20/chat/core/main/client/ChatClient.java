package cn.kk20.chat.core.main.client;

import cn.kk20.chat.core.config.ChatConfigBean;
import cn.kk20.chat.core.main.ClientComponent;
import cn.kk20.chat.core.main.Launcher;
import cn.kk20.chat.core.main.client.initializer.CenterClientChannelInitializer;
import cn.kk20.chat.core.main.client.initializer.ClientChannelInitializer;
import cn.kk20.chat.core.main.client.initializer.WebClientChannelInitializer;
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
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 聊天Server
 * @Author: Roy Z
 * @Date: 2020/2/17 16:00
 * @Version: v1.0
 */
@ClientComponent
public class ChatClient implements Launcher {
    private final Logger logger = LoggerFactory.getLogger(ChatClient.class);

    // 配置参数
    @Autowired
    ChatConfigBean chatConfigBean;
    @Autowired
    ClientChannelInitializer clientChannelInitializer;
    @Autowired
    WebClientChannelInitializer webClientChannelInitializer;
    @Autowired
    CenterClientChannelInitializer centerClientChannelInitializer;

    // 聊天服务器使用
    private ScheduledExecutorService commonServerExecutor = null, webServerExecutor = null;
    private EventLoopGroup commonServerParentGroup, webServerParentGroup, commonServerChildGroup, webServerChildGroup;
    // 中心服务器使用
    private ScheduledExecutorService centerServerExecutor = null;
    private NioEventLoopGroup nioEventLoopGroup = null;
    private Channel serverChannel = null;
    private boolean connectSuccess = false;

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
                        .childHandler(clientChannelInitializer);
                Channel channel = serverBootstrap.bind(chatConfigBean.getClient().getCommonServer().getPort())
                        .sync().channel();
                channel.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                commonServerParentGroup.shutdownGracefully();
                commonServerChildGroup.shutdownGracefully();
            }
        }, 0, chatConfigBean.getClient().getCommonServer().getAutoRestartTimeInterval(), TimeUnit.SECONDS);
        // web服务器
        webServerExecutor.scheduleWithFixedDelay(() -> {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            webServerParentGroup = new NioEventLoopGroup(1);
            webServerChildGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
            try {
                serverBootstrap.group(webServerParentGroup, webServerChildGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(webClientChannelInitializer);
                Channel channel = serverBootstrap.bind(chatConfigBean.getClient().getWebServer().getPort())
                        .sync().channel();
                channel.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                webServerParentGroup.shutdownGracefully();
                webServerChildGroup.shutdownGracefully();
            }
        }, 0, chatConfigBean.getClient().getWebServer().getAutoRestartTimeInterval(), TimeUnit.SECONDS);
        // 中心服务器
        centerServerExecutor.scheduleWithFixedDelay(() -> {
            logger.debug("连接中心服务器：执行一次");
            nioEventLoopGroup = new NioEventLoopGroup(1);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(centerClientChannelInitializer);
            // 发起异步连接操作
            try {
                ChannelFuture channelFuture = bootstrap.connect(chatConfigBean.getClient().getCenter().getHost(),
                        chatConfigBean.getClient().getCenter().getPort());
                connectSuccess = true;
                // 服务器同步连接断开时,这句代码才会往下执行
                channelFuture.channel().closeFuture().sync();
                logger.debug("连接中心服务器：正常关闭");
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.debug("连接中心服务器：发生异常");
            } finally {
                connectSuccess = false;
                boolean shutdown = nioEventLoopGroup.isShutdown();
                boolean shuttingDown = nioEventLoopGroup.isShuttingDown();
                boolean terminated = nioEventLoopGroup.isTerminated();
                if (!shutdown) {
                    nioEventLoopGroup.shutdownGracefully();
                }
                nioEventLoopGroup = null;
                logger.debug("连接中心服务器：shutdown={}，shuttingDown={}，terminated={}",
                        shutdown, shuttingDown, terminated);
            }
        }, 0, chatConfigBean.getClient().getCenter().getAutoRestartTimeInterval(), TimeUnit.SECONDS);
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
