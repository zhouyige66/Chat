package cn.kk20.chat.client;

import cn.kk20.chat.client.config.ChatParameterBean;
import cn.kk20.chat.client.initializer.CenterClientChannelInitializer;
import cn.kk20.chat.client.initializer.ClientChannelInitializer;
import cn.kk20.chat.client.initializer.WebClientChannelInitializer;
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
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 聊天Server
 * @Author: Roy
 * @Date: 2020/2/17 16:00
 * @Version: v1.0
 */
@Component
public class ClientServer implements Launcher {
    private final Logger logger = LoggerFactory.getLogger(ClientServer.class);

    // 配置参数
    @Autowired
    ChatParameterBean chatParameterBean;
    @Autowired
    ClientChannelInitializer clientChannelInitializer;
    @Autowired
    WebClientChannelInitializer webClientChannelInitializer;
    @Autowired
    CenterClientChannelInitializer centerClientChannelInitializer;
    @Autowired
    ChannelManager channelManager;

    // 聊天服务器使用
    private ScheduledExecutorService commonServerExecutor = null, webServerExecutor = null;
    private EventLoopGroup commonServerParentGroup, webServerParentGroup, commonServerChildGroup, webServerChildGroup;
    // 中心服务器使用
    private ScheduledExecutorService centerServerExecutor = null;
    private NioEventLoopGroup nioEventLoopGroup = null;
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
                Channel channel = serverBootstrap.bind(chatParameterBean.getCommonServer().getPort())
                        .sync().channel();
                channel.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                commonServerParentGroup.shutdownGracefully();
                commonServerChildGroup.shutdownGracefully();
            }
        }, 0, chatParameterBean.getCommonServer().getAutoRestartTimeInterval(), TimeUnit.SECONDS);
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
                Channel channel = serverBootstrap.bind(chatParameterBean.getWebServer().getPort())
                        .sync().channel();
                channel.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                webServerParentGroup.shutdownGracefully();
                webServerChildGroup.shutdownGracefully();
            }
        }, 0, chatParameterBean.getWebServer().getAutoRestartTimeInterval(), TimeUnit.SECONDS);
        // 中心服务器
        centerServerExecutor.scheduleWithFixedDelay(() -> {
            logger.info("连接中心服务器：执行一次");
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
                ChannelFuture channelFuture = bootstrap.connect(chatParameterBean.getCenterServer().getHost(),
                        chatParameterBean.getCenterServer().getPort());
                channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if (future.isSuccess()) {
                            ChannelFuture cf = (ChannelFuture) future;
                            connectSuccess = true;
                            channelManager.setCenterChannel(cf.channel());
                        }
                    }
                });
                Channel channel = channelFuture.channel();
                // 服务器同步连接断开时,这句代码才会往下执行
                channel.closeFuture().sync();
                logger.info("连接中心服务器：正常关闭");
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.info("连接中心服务器：发生异常");
            } finally {
                channelManager.setCenterChannel(null);
                connectSuccess = false;
                boolean shutdown = nioEventLoopGroup.isShutdown();
                boolean shuttingDown = nioEventLoopGroup.isShuttingDown();
                boolean terminated = nioEventLoopGroup.isTerminated();
                if (!shutdown) {
                    nioEventLoopGroup.shutdownGracefully();
                }
                nioEventLoopGroup = null;
                logger.info("连接中心服务器：shutdown={}，shuttingDown={}，terminated={}",
                        shutdown, shuttingDown, terminated);
            }
        }, 0, chatParameterBean.getCenterServer().getAutoRestartTimeInterval(), TimeUnit.SECONDS);
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

        // 收尾
        channelManager.clear();
    }

    @Override
    public boolean isLaunch() {
        return connectSuccess;
    }

}
