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
                Log4j.log.info("The server has a new file: " + fm.getFilename(),fm.getOwner());
                sendSoulFileMessage(fm, ctx);
            }
            if (msg instanceof SoulFileRequest){
                SoulFileRequest sfr = (SoulFileRequest) msg;
                sendSoulFileMessage(sfr,ctx);
            }
            if (msg instanceof AuthenticationRequest){
                AuthenticationRequest ar = (AuthenticationRequest) msg;
                String nickname = DBConnection.getNicknameByLoginAndPassword(ar.getLogin(),ar.getPassword());
                AuthenticationCommand ac = new AuthenticationCommand(nickname);
                Log4j.log.info("Send AuthenticationCommand with: " + nickname);
                ctx.writeAndFlush(ac);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void sendSoulFileMessage(AbstractMessage abstractMessage, ChannelHandlerContext ctx) throws Exception {
        ArrayList<String> filenames = new ArrayList<>();
        Files.list(Paths.get(abstractMessage.getOwner().getServerPath())).map(p -> p.getFileName().toString()).forEach(filenames::add);
        SoulFile sf = new SoulFile(filenames);
        ctx.writeAndFlush(sf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
