package com.example.demo.Controllers;

import com.example.demo.helper.MapHelper;
import com.example.demo.model.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AlbumController {

    @Autowired
    AlbumService albumService;

    // only singerId is shown in this api
    @RequestMapping("/albums")
    public Map getAllAlbums(){
        Map<String, Object> map = new HashMap();
        map.put("albums", albumService.getAllAlbum());
        return map;
    }


    @RequestMapping(value = "/newAlbumWithSigner/{singerId}/{albumTitle}", method = RequestMethod.POST)
    // use this when the singer of the album was already created
    public Map addNewAlbumWithSinger(@PathVariable long singerId,
                                              @PathVariable String albumTitle){

        return MapHelper.mapOf("album", albumService.addAlbumWithSingerId(singerId, albumTitle));

    }

    @RequestMapping(value = "/updateAlbum/{albumId}/{albumTitle}")
    public Map updateAlbum(@PathVariable long albumId,
                           @PathVariable String albumTitle){

        Map<String, Album> map = new HashMap<>();
        map.put("album", albumService.updateAlbum(albumId, albumTitle));
        return map;
    }

    @RequestMapping("/deleteSingleAlbum/{albumId}")
    public Map<String, Object> deleteSingleAlbum(@PathVariable long albumId) {
        Album album = albumService.deleteSingleAlbum(albumId);
        return MapHelper.mapOf("album", album);
    }

    // ref: https://stackoverflow.com/questions/9623258/passing-an-array-or-list-to-pathvariable-spring-java
    @RequestMapping("/testArray/{array}")
    public void testing(@PathVariable long[] array){

        System.out.println(array[1]);
    }

}
