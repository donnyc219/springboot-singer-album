package com.example.demo.model;

import com.google.cloud.ByteArray;

import java.util.List;

public class Singer {
    private String firstname;
    private String lastname;
    private ByteArray singleInfo;
    private long singerId;
    private List<Album> albums;

    public long getSingerId() {
        return singerId;
    }

    public void setSingerId(long singerId) {
        this.singerId = singerId;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public Singer(long singerId, String firstname, String lastname, ByteArray singleInfo, List<Album> albums) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.singleInfo = singleInfo;
        this.singerId = singerId;
        this.albums = albums;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public ByteArray getSingleInfo() {
        return singleInfo;
    }

    public void setSingleInfo(ByteArray singleInfo) {
        this.singleInfo = singleInfo;
    }
}
