package org.example.Sockets.Server;

import org.example.Core.Repository.DBRepository;
import org.example.Core.Models.Album;
import org.example.Core.Models.Artist;
import org.example.Core.Repository.Repository;
import org.example.Sockets.Utils.Serialization;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        Repository innerRepository = new DBRepository("jdbc:postgresql://localhost:5432/dev", "postgres", "postgres");
        RemoteServerRepository repository = new RemoteServerRepository(innerRepository, 8008);
        repository.start();
    }
}
