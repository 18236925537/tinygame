package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @author zhoupengbing
 * @packageName org.tinygame.herostory
 * @email zhoupengbing@telecomyt.com.cn
 * @description 消息编码器
 * @createTime 2020年07月13日 00:14:00
 * @Version v1.0
 */
public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgEncoder.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        
       if(msg == null || !(msg instanceof GeneratedMessageV3)){
          super.write(ctx,msg, promise);
          return;
       }

       int msgCode = -1;

       if(msg instanceof GameMsgProtocol.UserEntryResult){
           msgCode = GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE;
       }else{
           LOGGER.error("无法识别的消息类型,msgClazz=" + msg.getClass().getName());
       }

        byte[] bytes = ((GeneratedMessageV3) msg).toByteArray();
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeShort((short)0);
        buffer.writeShort((short)msgCode);
        buffer.writeBytes(bytes);

        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(buffer);
        super.write(ctx, frame, promise);

    }
    
}
