package GreenCode.server;

import GreenCode.common.FileCommand;
import GreenCode.common.FileMessage;
import GreenCode.common.FileRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MainHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            if (msg instanceof FileRequest){
                FileRequest fr = (FileRequest) msg;
                if (Files.exists(Paths.get(User.getServerPath() + fr.getFilename()))){
                    FileMessage fm = new FileMessage(Paths.get(User.getServerPath() + fr.getFilename()));
                    ctx.writeAndFlush(fm);
                }
            }
            if (msg instanceof FileMessage){
                FileMessage fm = (FileMessage) msg;
                Files.write(Paths.get(User.getServerPath() + fm.getFilename()),fm.getData(), StandardOpenOption.CREATE);
                FileCommand fc = new FileCommand("The server has a new file: " + fm.getFilename());
                ctx.writeAndFlush(fc);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
