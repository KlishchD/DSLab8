package org.example.Core.Models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Artist implements Serializable {
    public static final String ARTISTS = "Artists";
    public static final String ARTIST = "Artist";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String ALBUMS = "Albums";

    private int id;
    private String name;
    private int age;
    private List<Album> albums = new ArrayList<>();

    public boolean hasAlbum(Album album) {
        return albums.contains(album);
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public void removeAlbum(Album album) {
        albums.remove(album);
    }

    public Album getAlbumByIndex(int index) {
        return albums.get(index);
    }

    public Album getAlbumById(int id) {
        Album result = null;
        for (Album album: albums) {
            result = album;
        }
        return result;
    }

    public Album getLastAlbum() {
        return albums.isEmpty() ? null : albums.get(albums.size() - 1);
    }

    public boolean hasAlbums() {
        return !albums.isEmpty();
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", albums=" + albums +
                '}';
    }

}
