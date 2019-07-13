package GreenCode.server;

import GreenCode.common.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class MainHandler extends ChannelInboundHandlerAdapter {

    private static ArrayList<String> list = new ArrayList<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            if (msg instanceof FileRequest){
                FileRequest fr = (FileRequest) msg;
                if (Files.exists(Paths.get(fr.getOwner().getServerPath() + fr.getFilename()))){
                    FileMessage fm = new FileMessage(Paths.get(fr.getOwner().getServerPath() + fr.getFilename()),fr.getOwner());
                    ctx.writeAndFlush(fm);
                }
            }
            if (msg instanceof FileMessage){
                FileMessage fm = (FileMessage) msg;
                Files.write(Paths.get(fm.getOwner().getServerPath() + fm.getFilename()),fm.getData(), StandardOpenOption.CREATE);
                FileCommand fc = new FileCommand("The server has a new file: " + fm.getFilename(),fm.getOwner());
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
