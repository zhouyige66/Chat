package cn.kk20.chat.core.main.client;

import cn.kk20.chat.core.coder.CoderType;
import cn.kk20.chat.core.initializer.ServerChannelInitializer;
import cn.kk20.chat.core.initializer.WebServerChannelInitializer;
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
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 聊天Server
 * @Author: Roy Z
 * @Date: 2020/2/17 16:00
 * @Version: v1.0
 */
@Component
public final class ChatClient {
    // 配置属性
    private String host;
    private int port;
    private long reconnectTime;
    private CoderType coderType;
    // 其他属性
    private ScheduledExecutorService commonServerExecutor = null;
    private ScheduledExecutorService webServerExecutor = null;
    private ScheduledExecutorService connectToServerExecutor = null;

    private EventLoopGroup commonServerParentGroup, webServerParentGroup;
    private EventLoopGroup commonServerChildGroup, webServerChildGroup;
    private NioEventLoopGroup nioEventLoopGroup = null;
    private Channel serverChannel = null;

    public void launch() {
        ScheduledExecutorService commonExecutorService = Executors.newSingleThreadScheduledExecutor();
        ScheduledExecutorService webExecutorService = Executors.newSingleThreadScheduledExecutor();
        ScheduledExecutorService webExecutorService = Executors.newSingleThreadScheduledExecutor();
        commonExecutorService.scheduleWithFixedDelay(() -> {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            commonServerParentGroup = new NioEventLoopGroup(1);
            commonServerChildGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
            try {
                serverBootstrap.group(commonServerParentGroup, commonServerChildGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new ServerChannelInitializer(coderType));
                Channel channel = serverBootstrap.bind(commonServerPort).sync().channel();
                channel.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                commonServerParentGroup.shutdownGracefully();
                commonServerChildGroup.shutdownGracefully();
            }
        }, 0, 5, TimeUnit.SECONDS);
        webExecutorService.scheduleWithFixedDelay(() -> {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            webServerParentGroup = new NioEventLoopGroup(1);
            webServerChildGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
            try {
                serverBootstrap.group(webServerParentGroup, webServerChildGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new WebServerChannelInitializer());
                Channel channel = serverBootstrap.bind(webServerPort).sync().channel();
                channel.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                webServerParentGroup.shutdownGracefully();
                webServerChildGroup.shutdownGracefully();
            }
        }, 0, 5, TimeUnit.SECONDS);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            NioEventLoopGroup group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ServerChannelInitializer(coderType));
            // 发起异步连接操作
            try {
                ChannelFuture future = bootstrap.connect(host, port).sync();
                if (future.isSuccess()) {
                    connectSuccess = true;
                    serverChannel = future.channel();
                    // 服务器同步连接断开时,这句代码才会往下执行
                    serverChannel.closeFuture().sync();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                connectSuccess = false;
                group.shutdownGracefully();
            }
        }, 0, reconnectTime, TimeUnit.MILLISECONDS);
    }

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
    }

    public void start() {

    }

}
