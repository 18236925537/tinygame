package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @author zhoupengbing
 * @packageName org.tinygame.herostory
 * @email zhoupengbing@telecomyt.com.cn
 * @description 编码器
 * @createTime 2020年07月12日 23:40:00
 * @Version v1.0
 */
public class GameMsgDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!(msg instanceof BinaryWebSocketFrame)){
            return;
        }
        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf content = frame.content();
        //读取消息的长度
        content.readShort();
        //读取消息编号
        int msgCode = content.readShort();
        //读取真实的消息体字节数组
        byte [] msgBody = new byte[content.readableBytes()];
        content.readBytes(msgBody);

        GeneratedMessageV3 messageV3 = null;

        switch(msgCode){
            case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                messageV3 = GameMsgProtocol.UserEntryCmd.parseFrom(msgBody);
                break;
        }

        if(messageV3 != null){
            ctx.fireChannelRead(messageV3);
        }

    }
}
