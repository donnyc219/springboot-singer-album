package com.example.demo.Controllers;

import com.example.demo.helper.DatabaseHelper;
import com.example.demo.model.Album;
import com.google.cloud.spanner.KeySet;
import com.google.cloud.spanner.Mutation;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AlbumServiceImp implements AlbumService {

    @Autowired
    DatabaseHelper dbHelper;

    @Override
    public Album deleteSingleAlbum(long albumId) {
        Album album = getSingleAlbum(albumId);
        try {
            String sql = "DELETE FROM Albums WHERE SingerId="+ album.getSingerId() +" AND AlbumId="+ albumId +";";
            dbHelper.getDbClient().singleUse().executeQuery(Statement.of(sql));
            return album;
//            Mutation m = Mutation.delete("Albums", Key.of(4));
//            dbHelper.getDbClient().write(Arrays.asList(m));
//            return album;
        } catch (Exception ex) {
            System.err.format("No album found: %d\n", albumId);
            return null;
        }

    }

    public Album addAlbumWithSingerId(long singerId, String albumTitle) {
        long albumTableMaxId = dbHelper.getMaxId("Albums", "AlbumId");
        try {
//            Mutation.del
            Mutation m = Mutation.newInsertBuilder("Albums")
                    .set("SingerId").to(singerId)
                    .set("AlbumId").to(albumTableMaxId+1)
                    .set("AlbumTitle").to(albumTitle).build();
            List<Mutation> res = new ArrayList<>(Arrays.asList(m));
            dbHelper.getDbClient().write(res);
            return getSingleAlbum(albumTableMaxId+1);
        } catch (Exception ex) {
            System.err.format("Something wrong when adding a new album: %d (singerId)", singerId);
            return null;
        }

    }

    public Album getSingleAlbum(long albumId){
        String sql = "SELECT * FROM Albums WHERE AlbumId = " + albumId;
        ResultSet res = dbHelper.getDbClient().singleUse().executeQuery(Statement.of(sql));
        Album album = null;
        while (res.next()) {
            long sid = res.getLong("SingerId");
            long aid = res.getLong("AlbumId");
            String title = res.isNull("AlbumTitle")? null: res.getString("AlbumTitle");
            album = new Album(sid, aid, title);
        }
        return album;
    }


    public Album updateAlbum(long albumId, String albumTitle) {
        Album album = getSingleAlbum(albumId);
        try {
            Mutation m = Mutation.newUpdateBuilder("Albums")
                    .set("SingerId").to(album.getSingerId())
                    .set("AlbumId").to(albumId)
                    .set("AlbumTitle").to(albumTitle).build();

            List<Mutation> res = new ArrayList<>(Arrays.asList(m));

            dbHelper.getDbClient().write(res);
            album = getSingleAlbum(albumId);
        } catch (NullPointerException e) {
            System.err.format("Album not found with album id: %d\n", albumId);
        } catch (Exception e) {
            System.err.println("Something wrong when updating an album");
            System.err.println(e);
        }
        return album;
    }

    public List<Album> getAllAlbum(){
        List<Album> albums = null;
        String albumTitle;
        ResultSet res = dbHelper.getDbClient().singleUse()
                .read("Albums", KeySet.all(),Arrays.asList("SingerId", "AlbumId", "AlbumTitle"));
        if (res.next()) {
            albums = new ArrayList<>();
            do {
                albumTitle = res.isNull("AlbumTitle")? null: res.getString("AlbumTitle");
                albums.add(new Album(res.getLong("SingerId"),
                        res.getLong("AlbumId"),
                        albumTitle));

            } while (res.next());
        }
        while (res.next()){
            System.out.println(res.getString("AlbumTitle"));
        }

        return albums;
    }
}
