package org.example.Sockets.Client;

import org.example.Core.Models.Album;
import org.example.Core.Models.Artist;
import org.example.Core.Repository.Repository;
import org.example.Sockets.Utils.Serialization;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RemoteClientRepository implements Repository {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    public RemoteClientRepository(String host, int port) throws IOException {
        socket = new Socket(host, port);

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public int countArtists() {
        try {
            out.writeInt(0);
            return in.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countAlbums() {
        try {
            out.writeInt(1);
            return in.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertArtist(Artist artist) {
        try {
            out.writeInt(2);
            String bytes = Serialization.toString(artist);
            out.writeInt(bytes.length());
            out.writeBytes(bytes);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertAlbum(int artistId, Album album) {
        try {
            out.writeInt(3);
            out.writeInt(artistId);
            String bytes = Serialization.toString(album);
            out.writeInt(bytes.length());
            out.writeBytes(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteArtist(int id) {
        try {
            out.writeInt(4);
            out.writeInt(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAlbum(int id) {
        try {
            out.writeInt(5);
            out.writeInt(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Artist getArtist(int id) {
        try {
            out.writeInt(6);

            out.writeInt(id);

            int size = in.readInt();
            byte[] bytes = new byte[size];
            in.readFully(bytes);

            return (Artist) Serialization.fromBytes(bytes);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Album getAlbum(int id) {
        try {
            out.writeInt(7);
            out.writeInt(id);

            int size = in.readInt();
            byte[] bytes = new byte[size];
            in.readFully(bytes);

            return (Album) Serialization.fromBytes(bytes);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Artist> getArtist() {
        try {
            out.writeInt(8);

            int count = in.readInt();

            List<Artist> artists = new ArrayList<>(count);

            for (int i = 0; i < count; ++i) {
                int size = in.readInt();
                byte[] bytes = new byte[size];
                in.readFully(bytes);

                artists.add((Artist) Serialization.fromBytes(bytes));
            }

            return artists;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Album> getAlbums() {
        try {
            out.writeInt(9);

            int count = in.readInt();

            List<Album> albums = new ArrayList<>(count);

            for (int i = 0; i < count; ++i) {
                int size = in.readInt();
                byte[] bytes = new byte[size];
                in.readFully(bytes);

                albums.add((Album) Serialization.fromBytes(bytes));
            }

            return albums;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
