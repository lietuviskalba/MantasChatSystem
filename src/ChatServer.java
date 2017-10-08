import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    private static final int portNumber = 4444;//for this server

    private int serverPort;
    private List<ClientThread> clients; //store multiple clients

    public static void main(String[] args){
        ChatServer server = new ChatServer(portNumber);//contructor asks for a port number
        server.startServer();//this method is called to start the server
    }

    public ChatServer(int portNumber){
        this.serverPort = portNumber;
    }//contructor

    public List<ClientThread> getClients(){
        return clients;
    }// return all clients connected

    private void startServer(){
        clients = new ArrayList<ClientThread>();//store clients LOCALY in a arraylist
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);//waits for requests to come in BOUND to this port
            acceptClients(serverSocket);//call for multiple clients here
        } catch (IOException e){
            System.err.println("Could not listen on port: "+serverPort);
            System.exit(1);
        }
    }

    private void acceptClients(ServerSocket serverSocket){

        System.out.println("server starts port = " + serverSocket.getLocalSocketAddress());
        while(true){
            try{
                Socket socket = serverSocket.accept();//accept new client
                System.out.println("accepts : " + socket.getRemoteSocketAddress());
                ClientThread client = new ClientThread(this, socket);// iniciate client thread
                Thread thread = new Thread(client);//iniciate a thread
                thread.start();//start thread
                clients.add(client);//add clients to clientLIST thread
            } catch (IOException ex){
                System.out.println("Accept failed on : "+serverPort);
            }
        }
    }
}