package com.zengdw.base.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * netty客户端处理器
 * @author zengd
 * @version 1.0
 * @date 2022/8/26 15:11
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("连接成功");
        ctx.writeAndFlush("firstMessage");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        log.debug("收到消息：{}", msg);
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
