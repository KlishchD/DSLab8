package org.example.Sockets.Server;

import org.example.Core.Repository.DBRepository;
import org.example.Core.Repository.Repository;

import java.rmi.RemoteException;

public class Server {
    public static void main(String[] args) throws RemoteException {
        Repository innerRepository = new DBRepository("jdbc:postgresql://localhost:5432/dev", "postgres", "postgres");
        RemoteServerRepository repository = new RemoteServerRepository(innerRepository, 8008);
        repository.start();
    }
}
