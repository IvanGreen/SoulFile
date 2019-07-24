package GreenCode.common;

public class SoulFileRequest extends AbstractMessage {

    private User user;

    public SoulFileRequest(User user) {
        this.user = user;
    }

    public User getOwner() {
        return user;
    }

}
