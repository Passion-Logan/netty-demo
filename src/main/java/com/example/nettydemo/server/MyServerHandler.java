package com.example.nettydemo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 服务端处理器
 *
 * @author wql
 * @desc MyServerHandler
 * @date 2022/12/5
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2022/12/5
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送过来的消息
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("收到客户端" + ctx.channel().remoteAddress() + "发送的消息：" + byteBuf.toString(CharsetUtil.UTF_8));

        ctx.channel().eventLoop().execute(() -> {
            try {
                //长时间操作，不至于长时间的业务操作导致Handler阻塞
                Thread.sleep(1000);
                System.out.println("长时间的业务处理");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //发送消息给客户端
        ctx.writeAndFlush(Unpooled.copiedBuffer("服务端已收到消息，并给你发送一个问号?", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //发生异常，关闭通道
        ctx.close();
    }

}
