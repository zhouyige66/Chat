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
    private EventLoopGroup parentGroup1, parentGroup2;
    private EventLoopGroup childGroup1, childGroup2;

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

    public void launch(ApplicationContext context, int commonSocketPort, int webSocketPort) {
        this.context = context;

        // 启动服务器（接收Android、IOS端信息）
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                parentGroup1 = new NioEventLoopGroup(1);
                childGroup1 = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
                try {
                    serverBootstrap.group(parentGroup1, childGroup1)
                            .channel(NioServerSocketChannel.class)
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(new ServerChannelInitializer());
                    Channel channel = serverBootstrap.bind(commonSocketPort).sync().channel();
                    channel.closeFuture().sync();
                } catch (Exception e) {
                    throw new MessageException(e);
                } finally {
                    parentGroup1.shutdownGracefully();
                    childGroup1.shutdownGracefully();
                }
            }
        }).start();

        // 启动WebSocket服务器（接收网页端信息）
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                parentGroup2 = new NioEventLoopGroup(1);
                childGroup2 = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
                try {
                    serverBootstrap.group(parentGroup2, childGroup2)
                            .channel(NioServerSocketChannel.class)
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(new WebServerChannelInitializer());
                    Channel channel = serverBootstrap.bind(webSocketPort).sync().channel();
                    channel.closeFuture().sync();
                } catch (Exception e) {
                    throw new MessageException(e);
                } finally {
                    parentGroup2.shutdownGracefully();
                    childGroup2.shutdownGracefully();
                }
            }
        }).start();
    }

    public void stop() {
        if (parentGroup1 != null && !parentGroup1.isShutdown()) {
            parentGroup1.shutdownGracefully();
        }
        if (childGroup1 != null && !childGroup1.isShutdown()) {
            childGroup1.shutdownGracefully();
        }
        if (parentGroup2 != null && !parentGroup2.isShutdown()) {
            parentGroup2.shutdownGracefully();
        }
        if (childGroup2 != null && !childGroup2.isShutdown()) {
            childGroup2.shutdownGracefully();
        }
    }

    public ApplicationContext getContext() {
        return context;
    }

}
