package org.example.Sockets.Client;

import java.io.*;

public class Client {
    public static void main(String[] args) {
        try {
            RemoteClientRepository repository = new RemoteClientRepository("localhost", 8080);

            System.out.println("Artists: " + repository.countArtists());
            System.out.println("Albums: " + repository.countAlbums());

           /* Artist artist = new Artist();
            artist.setName("The remote one");
            artist.setAge(20);

            repository.insertArtist(artist);

            Album album = new Album();
            album.setName("Album am I");
            repository.insertAlbum(0, album);
            repository.deleteArtist(1);
            repository.deleteAlbum(10);
*/
            System.out.println("Artist: " + repository.getArtist(0));
            System.out.println("Album: " + repository.getAlbum(13));

            System.out.println("Artists: " + repository.getArtist());
            System.out.println("Albums: " + repository.getAlbums());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
