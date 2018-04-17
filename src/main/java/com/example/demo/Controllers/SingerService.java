package com.example.demo.Controllers;

import com.example.demo.model.Singer;
import com.google.cloud.ByteArray;

import java.util.List;

public interface SingerService {
    public Singer addNewSinger(String fName, String lName, ByteArray sInfo);
    public List<Singer> getAllSingersWithoutAlbums();
//    public List<Singer> getAllSingerWithAlbum();
    public Singer updateSinger(Long singerId, String firstname, String lastname, ByteArray singerInfo);
    public Singer getSingleSinger(Long singerId);
    public Singer deleteSingleSinger(long singerId);

}
