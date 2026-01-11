package com.boredxgames.tictactoeclient.domain.services.communication;


/**
 *
 * @author Hazem
 */

public class Header {

    private MessageType msgType;
    private Action action;

    public Header() {
    }

    public Header(MessageType msgType, Action action) {
        this.msgType = msgType;
        this.action = action;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(MessageType msgType) {
        this.msgType = msgType;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
