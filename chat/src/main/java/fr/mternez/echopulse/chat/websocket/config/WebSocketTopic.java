package fr.mternez.echopulse.chat.websocket.config;

public enum WebSocketTopic {

    SEND_MESSAGE_ON_CHANNEL("/channels/%s"),
    CHANNEL_CREATED("/servers/%s/channels/created"),
    CHANNEL_DELETED("/servers/%s/channels/deleted"),
    USER_JOINED_SERVER("/servers/%s/user-joined"),
    USER_LEFT_SERVER("/servers/%s/user-left");

    private final String topic;

    WebSocketTopic(final String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return this.topic;
    }
}
