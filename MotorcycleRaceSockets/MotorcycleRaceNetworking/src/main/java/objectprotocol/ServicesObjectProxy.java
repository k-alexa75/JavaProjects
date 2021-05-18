package objectprotocol;

import Domain.*;
import Services.IObserver;
import Services.IService;
import Services.RaceException;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServicesObjectProxy implements IService {
    private String host;
    private int port;

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    //private List<Response> responses;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesObjectProxy(String host, int port) {

        this.host = host;
        this.port = port;
        //responses = new ArrayList<>();
        qresponses = new LinkedBlockingQueue<>();
    }

    @Override
    public void login(User user, IObserver client) throws RaceException {
        initializeConnection();

        sendRequest(new GetUserRequest(user.getUsername(), user.getPassword()));
        Response response = readResponse();
        if (response instanceof GetUserResponse) {
            GetUserResponse getUserResponse = (GetUserResponse) response;
            User user1 = getUserResponse.getUser();
            System.out.println("user in server proxy");
            System.out.println(user1);
            sendRequest(new LogInRequest(user1));
            response = readResponse();
            if (response instanceof OkResponse) {
                this.client = client;
                return;
            }
        }
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            closeConnection();
            throw new RaceException(err.getMessage());
        }

    }

    @Override
    public void logout(User user, IObserver client) throws RaceException {
        sendRequest(new LogOutRequest(user));
        Response response = readResponse();
        closeConnection();
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            throw new RaceException(err.getMessage());
        }
    }

    @Override
    public User getUserByUsernameAndPassword(String username, String password) throws RaceException {
        sendRequest(new GetUserRequest(username, password));
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            throw new RaceException(err.getMessage());
        }
        if (response instanceof GetUserResponse) {
            GetUserResponse getUserResponse = (GetUserResponse) response;
            System.out.println("User in proxy: ");
            System.out.println(getUserResponse.getUser());
            return getUserResponse.getUser();
        }
        return null;
    }

    @Override
    public List<RowData> getRaces() throws RaceException{
        sendRequest(new GetRacesRequest());
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            throw new RaceException(err.getMessage());
        }
        if (response instanceof GetRacesResponse){
            GetRacesResponse getRacesResponse = (GetRacesResponse) response;
            List<RowData> races = getRacesResponse.getRaces();
            return races;
        }
        return null;
    }

    @Override
    public List<Rider> getRidersByTeam(String team) throws RaceException {
        sendRequest(new GetRidersByTeamRequest(team));
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            throw new RaceException(err.getMessage());
        }
        if (response instanceof GetRidersByTeamResponse) {
            GetRidersByTeamResponse getRidersByTeamResponse = (GetRidersByTeamResponse) response;
            List<Rider> riders = getRidersByTeamResponse.getRiders();
            System.out.println("Riders in getRiders by team: "+ riders);
            return riders;
        }
        return null;
    }

    @Override
    public void registerRider(Rider rider, User user) throws RaceException {
        sendRequest(new RegisterRiderRequest(rider, user));
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            throw new RaceException(err.getMessage());
        }
        if (response instanceof RegisterRiderResponse){
            return;
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws RaceException {
        try {
            output.writeObject(request);
            //output.reset();
            output.flush();
        } catch (IOException e) {
            throw new RaceException("Error sending object " + e);
        }

    }

    private Response readResponse() throws RaceException {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws RaceException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(UpdateResponse update) {
        if (update instanceof RiderRegisteredResponse){
            try{
                client.riderRegistered();
            }catch (RaceException exception){
                exception.printStackTrace();
            }
        }
    }


    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (response instanceof UpdateResponse) {
                        System.out.println("OK ...");
                        handleUpdate((UpdateResponse) response);
                    } else {
                        /*responses.add((Response) response);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        synchronized (responses) {
                            responses.notify();
                        }*/
                        try {
                            qresponses.put((Response) response);
                            System.out.println(qresponses);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
