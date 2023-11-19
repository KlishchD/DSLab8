package org.example.RMI.Client;

import org.example.Core.Repository.Repository;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            Repository repository = (Repository) registry.lookup("repository");

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
            System.out.println("Artist: " + repository.getArtists(0));
            System.out.println("Album: " + repository.getAlbum(13));

            System.out.println("Artists: " + repository.getArtists());
            System.out.println("Albums: " + repository.getAlbums());
        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
