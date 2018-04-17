package com.example.demo.Controllers;

import com.example.demo.model.Album;

import java.util.List;

public interface AlbumService {

    public Album addAlbumWithSingerId(long singerId, String albumTitle);
    public Album updateAlbum(long albumId, String albumTitle);
    public List<Album> getAllAlbum();
    public Album deleteSingleAlbum(long albumId);
}
