package org.example.Sockets.Server;

import org.example.Core.Models.Album;
import org.example.Core.Models.Artist;
import org.example.Core.Repository.Repository;
import org.example.Utils.Serialization;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;

public class RemoteServerRepository implements Repository {
    private final ServerSocket serverSocket;

    private final DataInputStream in;
    private final DataOutputStream out;

    private final Repository repository;

    public RemoteServerRepository(Repository repository, int port) {
        try {
            serverSocket = new ServerSocket(8080);

            Socket socket = serverSocket.accept();

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.repository = repository;
    }

    @Override
    public int countArtists() throws RemoteException {
        return repository.countArtists();
    }

    @Override
    public int countAlbums() throws RemoteException  {
        return repository.countAlbums();
    }

    @Override
    public void insertArtist(Artist artist) throws RemoteException  {
        repository.insertArtist(artist);
    }

    @Override
    public void insertAlbum(int artistId, Album album) throws RemoteException  {
        repository.insertAlbum(artistId, album);
    }

    @Override
    public void deleteArtist(int id) throws RemoteException  {
        repository.deleteArtist(id);
    }

    @Override
    public void deleteAlbum(int id) throws RemoteException  {
        repository.deleteAlbum(id);
    }

    @Override
    public Artist getArtists(int id) throws RemoteException  {
        return repository.getArtists(id);
    }

    @Override
    public Album getAlbum(int id) throws RemoteException  {
        return repository.getAlbum(id);
    }

    @Override
    public List<Artist> getArtists() throws RemoteException  {
        return repository.getArtists();
    }

    @Override
    public List<Album> getAlbums() throws RemoteException  {
        return repository.getAlbums();
    }

    void start() {
        try {
            while (true) {
                int operation = in.readInt();

                System.out.println("Received operation: " + operation);

                boolean isOk = switch (operation) {
                    case 0 -> {
                        out.writeInt(repository.countArtists());
                        out.flush();
                        yield true;
                    }
                    case 1 -> {
                        out.writeInt(repository.countAlbums());
                        out.flush();
                        yield true;
                    }
                    case 2 -> {
                        int size = in.readInt();
                        byte[] bytes = new byte[size];
                        in.readFully(bytes);
                        Artist artist = (Artist) Serialization.fromBytes(bytes);
                        repository.insertArtist(artist);

                        yield true;
                    }
                    case 3 -> {
                        int artistId = in.readInt();
                        int size = in.readInt();

                        byte[] bytes = new byte[size];
                        in.readFully(bytes);
                        Album album = (Album) Serialization.fromBytes(bytes);

                        repository.insertAlbum(artistId, album);

                        yield true;
                    }
                    case 4 -> {
                        int id = in.readInt();
                        repository.deleteArtist(id);

                        yield true;
                    }
                    case 5 -> {
                        int id = in.readInt();
                        repository.deleteAlbum(id);

                        yield true;
                    }
                    case 6 -> {
                        int id = in.readInt();
                        Artist artist = repository.getArtists(id);

                        String bytes = Serialization.toString(artist);
                        out.writeInt(bytes.length());
                        out.writeBytes(bytes);

                        yield true;
                    }
                    case 7 -> {
                        int id = in.readInt();
                        Album album = repository.getAlbum(id);

                        String bytes = Serialization.toString(album);
                        out.writeInt(bytes.length());
                        out.writeBytes(bytes);

                        yield true;
                    }
                    case 8 -> {
                        List<Artist> artists = repository.getArtists();

                        out.writeInt(artists.size());

                        for (Artist artist : artists) {
                            String bytes = Serialization.toString(artist);
                            out.writeInt(bytes.length());
                            out.writeBytes(bytes);
                        }

                        yield true;
                    }

                    case 9 -> {
                        List<Album> albums = repository.getAlbums();

                        out.writeInt(albums.size());

                        for (Album album : albums) {
                            String bytes = Serialization.toString(album);
                            out.writeInt(bytes.length());
                            out.writeBytes(bytes);
                        }

                        yield true;
                    }
                    default -> {
                        System.out.println("Unsupported operation");
                        yield false;
                    }
                };

                if (!isOk) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
