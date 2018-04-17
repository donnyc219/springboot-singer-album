package com.example.demo.Controllers;

import com.example.demo.helper.DatabaseHelper;
import com.example.demo.helper.MapHelper;
import com.example.demo.model.Singer;
import com.google.cloud.ByteArray;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SingerController {

    @Autowired
    private DatabaseHelper dbHelper;

    @Autowired
    private SingerService singerService;

    @RequestMapping("/singers")
    public String test(){
        return "You are in test()";
    }

    @RequestMapping(value="/newSinger", method = RequestMethod.POST)
    public ImmutableMap addSinger(@RequestParam(value="firstname", required = false) String firstname,
                            @RequestParam(value="lastname", required = false) String lastname,
                            @RequestParam(value="singleInfo", required = false) ByteArray singerInfo){


        Singer singer = singerService.addNewSinger(firstname, lastname, singerInfo);

        return ImmutableMap.of("status", "success", "singer", singer);
    }

    @RequestMapping(value="/singers", method = RequestMethod.GET)
    public ImmutableMap singers() {
        List<Singer> singers = singerService.getAllSingersWithoutAlbums();
        int count = singers.size();
        return ImmutableMap.of("count", count, "singers", singers);
    }

    @RequestMapping(value = "/updateSinger", method = RequestMethod.PUT)
    public Map<String, Object> updateSinger(@RequestParam(required = true) Long singerId,
                               @RequestParam(required = false) String firstname,
                               @RequestParam(required = false) String lastname,
                               @RequestParam(required = false) ByteArray singerInfo){

        Singer singer = singerService.updateSinger(singerId, firstname, lastname, singerInfo);
        Map<String, Object> map = new HashMap<>();
        map.put("singer", singer);
        return map;

    }

    @RequestMapping("/delete/{singerId}")
    public Map<String, Object> deleteSingleSinger(@PathVariable long singerId){
        Singer singer = singerService.deleteSingleSinger(singerId);
        return MapHelper.mapOf("singer", singer);
    }

    @RequestMapping("/deleteSingers/{singerIds}")
    public ImmutableMap deleteMultipleSingers(@PathVariable long[] singerIds){

        return null;
    }

//    @RequestMapping(value="/deleteSingers", method = RequestMethod.POST)
//    public String deleteSingers(@RequestParam Long[] singerIds) {
//        System.out.println("Controllers ids: " + singerIds.toString());
////        System.out.println("name: " + name);
//        return "done!";
//    }
}
