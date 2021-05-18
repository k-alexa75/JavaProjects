package utils;

import Services.IService;
import objectprotocol.ClientObjectWorker;

import java.net.Socket;

public class ObjectConcurrentServer extends AbstractConcurrentServer {
    private IService server;

    public ObjectConcurrentServer(int port, IService server) {
        super(port);
        this.server = server;
        System.out.println("Race - RaceObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientObjectWorker worker = new ClientObjectWorker(server, client);
        Thread tw = new Thread(worker);
        return tw;
    }


}
