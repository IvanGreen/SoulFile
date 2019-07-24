package GreenCode.common;

public class AuthenticationCommand extends AbstractMessage {

    String nickname;

    public AuthenticationCommand(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
