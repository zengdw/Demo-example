package com.zengdw.base.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * server端 处理器
 *
 * @author zengd
 * @version 1.0
 * @date 2022/8/26 15:04
 */
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("连接成功, ip:{}", ctx.channel().localAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        log.debug("收到消息：{}", msg);
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("连接断开");
    }
}
