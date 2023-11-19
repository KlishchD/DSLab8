package org.example.Core.Repository;

import org.example.Core.Models.Artist;
import org.example.Core.Models.Album;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBRepository extends UnicastRemoteObject implements Repository{
    private Connection connection;

    public DBRepository(String url, String user, String password) throws RemoteException {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);

            Statement statement = connection.createStatement();

            String artistsTable = "CREATE TABLE IF NOT EXISTS artists (id SERIAL PRIMARY KEY, name varchar(30), age int)";
            String albumsTable = "CREATE TABLE IF NOT EXISTS albums (id SERIAL PRIMARY KEY, artist_id int, name varchar(30), CONSTRAINT fk_artist FOREIGN KEY(artist_id) REFERENCES artists(id))";

            statement.execute(artistsTable);
            statement.execute(albumsTable);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countArtists() {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT COUNT(*) as count FROM artists";

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int countAlbums() {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT COUNT(*) as count FROM albums";

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public void insertArtist(Artist artist) {
        try {
            Statement statement = connection.createStatement();
            String artistQuery = "INSERT INTO artists(name, age) VALUES('" + artist.getName() + "'," + artist.getAge() + ")";
            statement.execute(artistQuery);

            String findQuery = "SELECT * FROM artists WHERE name='" + artist.getName() + "' AND age=" + artist.getAge();
            ResultSet resultSet = statement.executeQuery(findQuery);
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                for (Album album : artist.getAlbums()) {
                    String albumsQuery = "INSERT INTO albums(artist_id, name) VALUES(" + id + ",'" + album.getName() + "')";
                    statement.execute(albumsQuery);
                }
            }
        } catch (SQLException e) {
            System.out.println("Cannot add artist as it already exists");
        }
    }

    @Override
    public void insertAlbum(int artistId, Album album) {
        try {
            Statement statement = connection.createStatement();
            String albumsQuery = "INSERT INTO albums(artist_id, name) VALUES(" + artistId + ",'" + album.getName() + "')";
            statement.execute(albumsQuery);
        } catch (SQLException e) {
            System.out.println("Cannot add album as it already exists");
        }
    }

    @Override
    public void deleteArtist(int id) {
        try {
            Statement statement = connection.createStatement();
            String albumsQuery = "DELETE FROM albums WHERE artist_id = " + id;
            statement.execute(albumsQuery);
            String artistQuery = "DELETE FROM artists WHERE id = " + id;
            statement.execute(artistQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAlbum(int id) {
        try {
            Statement statement = connection.createStatement();
            String albumsQuery = "DELETE FROM albums WHERE id = " + id;
            statement.execute(albumsQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Artist getArtists(int id) {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM (SELECT * FROM artists WHERE id = " + id + ") JOIN (SELECT id as album_id, artist_id as id, name as album_name FROM albums) USING (id)";

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                Artist artist = new Artist();
                artist.setId(resultSet.getInt("id"));
                artist.setName(resultSet.getString("name"));
                artist.setAge(resultSet.getInt("age"));

               do {
                    Album album = new Album();
                    album.setId(resultSet.getInt("album_id"));
                    album.setName(resultSet.getString("album_name"));
                    artist.addAlbum(album);
               } while (resultSet.next());

               return artist;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Album getAlbum(int id) {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM albums WHERE id=" + id;

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                Album album = new Album();
                album.setId(resultSet.getInt("id"));
                album.setName(resultSet.getString("name"));
                return album;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Artist> getArtists() {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM (SELECT * FROM artists) LEFT JOIN (SELECT id as album_id, artist_id as id, name as album_name FROM albums) USING (id)";

            List<Artist> artists = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Artist artist = null;

                do {
                    if (artist == null || resultSet.getInt("id") != artist.getId()) {
                        if (artist != null) {
                            artists.add(artist);
                        }

                        artist = new Artist();
                        artist.setId(resultSet.getInt("id"));
                        artist.setName(resultSet.getString("name"));
                        artist.setAge(resultSet.getInt("age"));
                    }

                    Album album = new Album();
                    album.setId(resultSet.getInt("album_id"));
                    if (resultSet.wasNull()) {
                        continue;
                    }
                    album.setName(resultSet.getString("album_name"));
                    artist.addAlbum(album);
                } while (resultSet.next());

                artists.add(artist);
            }

            return artists;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Album> getAlbums() {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM albums";

            List<Album> albums = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Album album = new Album();
                album.setId(resultSet.getInt("id"));
                album.setName(resultSet.getString("name"));
                albums.add(album);
            }

            return albums;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
