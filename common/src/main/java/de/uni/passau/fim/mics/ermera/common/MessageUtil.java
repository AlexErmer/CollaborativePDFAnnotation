package de.uni.passau.fim.mics.ermera.common;

import java.util.ArrayList;
import java.util.List;

public class MessageUtil {
    public static final String NAME = "messageUtil";

    private List<Message> messageList = new ArrayList();

    public void addMessage(MessageTypes type, String msg) {
        messageList.add(new Message(type, msg));
    }

    public List<Message> getMessages() {
        List<Message> ret = messageList;
        messageList = new ArrayList();
        return ret;
    }

    public class Message {
        private MessageTypes type;
        private String msg;

        private Message() { }

        private Message(MessageTypes type, String msg) {
            this.type = type;
            this.msg = msg;
        }

        public MessageTypes getType() {
            return type;
        }

        public void setType(MessageTypes type) {
            this.type = type;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
