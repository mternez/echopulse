package fr.mternez.echopulse.chat.kafka.config;

public enum KafkaTopic {

    SEND_MESSAGE_ON_CHANNEL("channels.send-message"),
    CREATE_CHANNEL("channels.create"),
    DELETE_CHANNEL("channels.delete"),
    USER_JOIN_SERVER("user.join-server"),
    USER_LEAVE_SERVER("user.leave-server");

    private final String topic;

    KafkaTopic(final String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return this.topic;
    }
}
