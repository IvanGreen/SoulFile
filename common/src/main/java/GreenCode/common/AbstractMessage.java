package GreenCode.common;

import java.io.Serializable;

public abstract class AbstractMessage implements Serializable {
    private User owner;

    public AbstractMessage() {
    }

    public AbstractMessage(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }
}
