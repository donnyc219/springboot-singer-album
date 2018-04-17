package com.example.demo.model;

public class Album {
    private long singerId;
    private long albumId;
    private String albumTile;

    public Album(long singerId, long albumId, String albumTile) {
        this.singerId = singerId;
        this.albumId = albumId;
        this.albumTile = albumTile;
    }

    public long getSingerId() {
        return singerId;
    }

    public void setSingerId(long singerId) {
        this.singerId = singerId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumTile() {
        return albumTile;
    }

    public void setAlbumTile(String albumTile) {
        this.albumTile = albumTile;
    }
}
