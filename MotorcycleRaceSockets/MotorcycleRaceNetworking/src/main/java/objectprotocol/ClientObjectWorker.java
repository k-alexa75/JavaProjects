package objectprotocol;

import Domain.Race;
import Domain.Rider;
import Domain.RowData;
import Domain.User;
import Services.IObserver;
import Services.IService;
import Services.RaceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientObjectWorker implements Runnable, IObserver {
    private IService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientObjectWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request = input.readObject();
                Object response = handleRequest((Request)request);
                if (response!= null){
                    sendResponse((Response) response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private Response handleRequest(Request request){
        Response response=null;

        if (request instanceof LogInRequest){
            System.out.println("Login request ...");
            LogInRequest logReq = (LogInRequest)request;
            User user = logReq.getUser();
            System.out.println("user in handleRequest" + user);
            try {
                server.login(user, this);
                return new OkResponse();
            } catch (RaceException exception) {
                connected = false;
                return new ErrorResponse(exception.getMessage());
            }
        }
        if (request instanceof LogOutRequest){
            System.out.println("Logout request");
            LogOutRequest logReq=(LogOutRequest)request;
            User user = logReq.getUser();
            try {
                server.logout(user, this);
                connected=false;
                return new OkResponse();

            } catch (RaceException exception) {
                return new ErrorResponse(exception.getMessage());
            }
        }
        if (request instanceof GetUserRequest){
            System.out.println("Get User Request ...");

            GetUserRequest getUserRequest = (GetUserRequest) request;
            String username = getUserRequest.getUsername();
            String password = getUserRequest.getPassword();
            System.out.println("username in getUserRequest: "+username);
            System.out.println("password in getUserRequest: "+password);
            try{
                User user = server.getUserByUsernameAndPassword(username, password);
                System.out.println("User in getUserRequest: "+user);
                return new GetUserResponse(user);
            }catch (RaceException exception) {
                connected = false;
                return new ErrorResponse(exception.getMessage());
            }
        }
        if (request instanceof GetRacesRequest){
            System.out.println("Get all races request ...");

            GetRacesRequest getAllRacesRequest = (GetRacesRequest) request;
            try{
                List<RowData> races = server.getRaces();
                System.out.println("Races in getAllRacesRequest: " + races);
                return new GetRacesResponse(races);
            }catch (RaceException exception){
                connected = false;
                return new ErrorResponse(exception.getMessage());
            }
        }
        if (request instanceof GetRidersByTeamRequest){
            System.out.println("Get riders by team request ...");

            GetRidersByTeamRequest getRidersByTeamRequest = (GetRidersByTeamRequest) request;
            try{
                String team = getRidersByTeamRequest.getTeam();
                List<Rider> riders = server.getRidersByTeam(team);
                return new GetRidersByTeamResponse(riders);
            }catch (RaceException exception){
                connected = false;
                return new ErrorResponse(exception.getMessage());
            }
        }
        if (request instanceof RegisterRiderRequest){
            System.out.println("Register rider request: ");

            RegisterRiderRequest registerRiderRequest = (RegisterRiderRequest) request;
            try{
                Rider rider = registerRiderRequest.getRider();
                User user = registerRiderRequest.getUser();
                server.registerRider(rider, user);
                return new RegisterRiderResponse();
            } catch (RaceException exception) {
                connected = false;
                return new ErrorResponse(exception.getMessage());
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        //output.reset();
        output.flush();
    }

    @Override
    public void riderRegistered() throws RaceException {
        System.out.println("Rider registered !!");
        try{
            sendResponse(new RiderRegisteredResponse());
        }catch (IOException exception){
            throw new RaceException("Sending error: "+ exception);
        }
    }
}

