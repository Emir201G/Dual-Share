package com.emir201.dualshare.model;

public class CardItem {

    public enum MediaType {
        IMAGE,
        VIDEO,
        AUDIO,
    }

    private MediaType type;
    private String title;
    private String uri;

    public CardItem(MediaType type, String title, String uri) {
        this.type = type;
        this.title = title;
        this.uri = uri;
    }

    public MediaType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }
}
