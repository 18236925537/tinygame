package org.tinygame.herostory.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @author zhoupengbing
 * @packageName org.tinygame.herostory.handler
 * @email zhoupengbing@telecomyt.com.cn
 * @description 消息处理类
 * @createTime 2020年07月12日 22:05:00
 * @Version v1.0
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgHandler.class);

    /**
     * 客户端信道数组,一定要使用static，否则无法实现群发
     */
    private static final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 新的客户端连接上之后，加入到群组中去
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        _channelGroup.add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        LOGGER.info("收到客户端消息" + o);
        if(o instanceof GameMsgProtocol.UserEntryCmd){
            //取出CMD参数
            GameMsgProtocol.UserEntryCmd cmd = (GameMsgProtocol.UserEntryCmd)o;
            GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
            resultBuilder.setUserId(cmd.getUserId());
            resultBuilder.setHeroAvatar(cmd.getHeroAvatar());
            GameMsgProtocol.UserEntryResult newResult = resultBuilder.build();
            //构建结果并发送
            _channelGroup.writeAndFlush(newResult);
        }
    }
    
}
