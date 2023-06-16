package com.codescafe.kissanformer.model;

public class ModelTeamChat {

    String sender,receiver,msg,type,timestamp,isSeen,starred,key,voice;

    public ModelTeamChat(String sender, String receiver, String msg, String type, String timestamp, String isSeen, String starred, String key) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.type = type;
        this.timestamp = timestamp;
        this.isSeen = isSeen;
        this.starred = starred;
        this.key = key;
    }

    public ModelTeamChat(String sender, String receiver, String msg, String type, String timestamp, String isSeen, String starred, String key, String voice) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.type = type;
        this.timestamp = timestamp;
        this.isSeen = isSeen;
        this.starred = starred;
        this.key = key;
        this.voice = voice;
    }

    public ModelTeamChat() {
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }

    public String getStarred() {
        return starred;
    }

    public void setStarred(String starred) {
        this.starred = starred;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
