package org.example.Core.Repository;

import org.example.Core.Models.Album;
import org.example.Core.Models.Artist;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Repository extends Remote {
    int countArtists() throws RemoteException;
    int countAlbums() throws RemoteException;

    void insertArtist(Artist artist) throws RemoteException;
    void insertAlbum(int artistId, Album album) throws RemoteException;

    void deleteArtist(int id) throws RemoteException;
    void deleteAlbum(int id) throws RemoteException;

    Artist getArtists(int id) throws RemoteException;
    Album getAlbum(int id) throws RemoteException;

    List<Artist> getArtists() throws RemoteException;
    List<Album> getAlbums() throws RemoteException;
}
