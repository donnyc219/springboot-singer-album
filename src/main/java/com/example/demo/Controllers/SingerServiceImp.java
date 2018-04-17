package com.example.demo.Controllers;

import com.example.demo.helper.DatabaseHelper;
import com.example.demo.model.Album;
import com.example.demo.model.Singer;
import com.google.cloud.ByteArray;
import com.google.cloud.spanner.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SingerServiceImp implements SingerService, InitializingBean {

    @Autowired
    private DatabaseHelper dbHelper;


    public Singer addNewSinger(String fName, String lName, ByteArray sInfo){
        long singerId = dbHelper.getMaxId("Singers", "SingerId")+1;
        Mutation.WriteBuilder writeBuilder = Mutation.newInsertBuilder("Singers")
                .set("SingerId").to(singerId);

        if (fName!=null){
            writeBuilder = writeBuilder.set("FirstName").to(fName);
        }
        if (lName!=null){
            writeBuilder = writeBuilder.set("LastName").to(lName);
        }
        if (sInfo!=null){
            writeBuilder = writeBuilder.set("SingleInfo").to(sInfo);
        }
        Mutation m = writeBuilder.build();
        List<Mutation> list = new ArrayList<Mutation>(Arrays.asList(m));
        dbHelper.getDbClient().write(list);
        return new Singer(singerId, fName, lName, sInfo, null);
    }

    @Override
    public Singer deleteSingleSinger(long singerId) {

        Singer singer = getSingleSinger(singerId);

        try {
            Mutation m = Mutation.delete("Singers", Key.of(singerId));
            dbHelper.getDbClient().write(Arrays.asList(m));
        } catch (Exception e) {
            System.err.format("No singer to delete: %d\n", singerId);
        }

        return singer;
    }


    public List<Singer> getAllSingersWithoutAlbums() {
        List<Singer> singers = new ArrayList<>();
        ResultSet res = dbHelper.getDbClient().singleUse().read("Singers",
                KeySet.all(),
                Arrays.asList("SingerId", "FirstName", "LastName", "SingleInfo"));
        Singer s;
        Long singerId;
        String firstname, lastname;
        ByteArray singerInfo;
        while (res.next()){
            singerId = res.getLong(0);
            firstname = res.isNull(1)? null: res.getString(1);
            lastname = res.isNull(2)? null: res.getString(2);
            singerInfo = res.isNull(3)? null: res.getBytes(3);
            s = new Singer(singerId, firstname, lastname, singerInfo, null);
            singers.add(s);
        }
        return singers;
    }

//    public List<Singer> getAllSingerWithAlbum(){
//        List<Singer> singers = new ArrayList<>();
//        Singer singer = null;
//        String sql = "SELECT a.SingerId as SingerId, AlbumId, FirstName, LastName, SingleInfo, AlbumTitle" +
//                "FROM Albums as a, Singers as s" +
//                "WHERE a.SingerId = s.SingerId;";
//        ResultSet res = dbHelper.getDbClient().singleUse().executeQuery(Statement.of(sql));
//
//        while (res.next()) {
//
//        }
//
//    }

    public String deleteSingers(List<Long> singerIds) {
        System.out.println("Controllers ids: " + singerIds.toString());
        return "done!";
    }

    public Singer updateSinger(Long singerId, String firstname, String lastname, ByteArray singerInfo) {
        Mutation.WriteBuilder m = Mutation.newUpdateBuilder("Singers")
                .set("SingerId").to(singerId);

        if (firstname!=null)    m.set("FirstName").to(firstname);
        if (lastname!=null)    m.set("LastName").to(lastname);
        if (singerInfo!=null)    m.set("SingleInfo").to(singerInfo);

        Singer singer = null;
        try {
            List<Mutation> res = Arrays.asList(m.build());
            dbHelper.getDbClient().write(res);
            singer = getSingleSinger(singerId);
        } catch (Exception e) {
            System.out.println("Singer not found");
        }

        return singer;
    }

    public Singer getSingleSinger(Long singerId) {
        String sql = "SELECT * FROM Singers WHERE SingerId = " + singerId;
        ResultSet rs = dbHelper.getDbClient().singleUse()
                .executeQuery(Statement.of(sql));

        Singer singer = null;
        String firstname, lastname;
        ByteArray singerInfo;
        List<Album> albums = null;
        Album album = null;

        while (rs.next()) {
            singerId = rs.getLong("SingerId");
            firstname = rs.isNull("FirstName")? null: rs.getString("FirstName");
            lastname = rs.isNull("LastName")? null: rs.getString("LastName");
            singerInfo = rs.isNull("SingleInfo")? null: rs.getBytes("SingleInfo");
            singer = new Singer(singerId, firstname, lastname, singerInfo, null);
        }
        rs.close();

        try {
            // add the albums
            sql = "SELECT AlbumId, AlbumTitle FROM Albums WHERE SingerId = " + singerId;
            rs = dbHelper.getDbClient().singleUse().executeQuery(Statement.of(sql));
            if (rs.next()) {
                albums = new ArrayList<>();

                do {
                    long albumId = rs.getLong("AlbumId");
                    String albumTitle = rs.isNull("AlbumTitle")? null: rs.getString("AlbumTitle");
                    album = new Album(singerId, albumId, albumTitle);
                    albums.add(album);
                } while (rs.next());
            }

            rs.close();
            singer.setAlbums(albums);
        } catch (NullPointerException ex) {
            System.err.format("No singer found: %d\n", singerId );
        }

        return singer;

    }

//    public Singer removeSingleSinger(Long singerId){
//
//    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
