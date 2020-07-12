package org.tinygame.herostory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import sun.misc.JavaAWTAccess;

/**
 * @author zhoupengbing
 * @packageName org.tinygame.herostory
 * @email zhoupengbing@telecomyt.com.cn
 * @description 服务器主函数
 * @createTime 2020年07月12日 18:25:00
 * @Version v1.0
 */
public class ServerMain {

    public static void main(String[] args) {
        //定义两个线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //添加两个线程组
        serverBootstrap.group(bossGroup,workerGroup);
        //服务端信道
        serverBootstrap.channel(NioServerSocketChannel.class);
        //客户端信道-处理器
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
              socketChannel.pipeline().addLast(
                      //服务端解码器
                      new HttpServerCodec(),
                      //消息长度限制
                      new HttpObjectAggregator(65535),
                      //websocket的server端
                      new WebSocketServerProtocolHandler("/websocket")
              );
            }
        });
        
        try {
            //绑定服务端口12345
            ChannelFuture future = serverBootstrap.bind(12345).sync();
            if(future.isSuccess()){
                System.out.println("服务器启动成功");
            }
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
