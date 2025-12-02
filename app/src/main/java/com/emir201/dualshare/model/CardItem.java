package com.emir201.dualshare.model;

import android.net.Uri;

public class CardItem {

    public enum Type {

        IMAGE,
        VIDEO
    }

    private Type type;
    private String uri;

    public CardItem(Type type, String uri) {
        this.type = type;
        this.uri = uri;
    }

    public Type getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }
}
