package org.example.RMI.Server;

import org.example.Core.Repository.DBRepository;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            DBRepository repository = new DBRepository("jdbc:postgresql://localhost:5432/dev", "postgres", "postgres");

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("repository", repository);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
