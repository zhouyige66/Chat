package cn.kk20.chat.core;

import cn.kk20.chat.core.exception.MessageException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.context.ApplicationContext;

public final class ChatServer {
    private static ChatServer instance;
    private ApplicationContext context;
    private EventLoopGroup commonServerParentGroup, webServerParentGroup;
    private EventLoopGroup commonServerChildGroup, webServerChildGroup;

    private ChatServer() {
    }

    public static ChatServer getInstance() {
        if (instance == null) {
            synchronized (ChatServer.class) {
                if (instance == null) {
                    instance = new ChatServer();
                }
            }
        }
        return instance;
    }

    public void launch(ApplicationContext context, int commonServerPort, ServerChannelInitializer.CoderEnum coderType,
                       int webServerPort) {
        this.context = context;

        // 启动服务器（接收Android、IOS端信息）
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                    throw new MessageException(e);
                } finally {
                    commonServerParentGroup.shutdownGracefully();
                    commonServerChildGroup.shutdownGracefully();
                }
            }
        }).start();

        // 启动WebSocket服务器（接收网页端信息）
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                    throw new MessageException(e);
                } finally {
                    webServerParentGroup.shutdownGracefully();
                    webServerChildGroup.shutdownGracefully();
                }
            }
        }).start();
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

    public ApplicationContext getContext() {
        return context;
    }

}
