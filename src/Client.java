import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String myIP = "192.168.0.102";
    private static final int portNumber = 4444;

    private String userName;
    private String clientIP;
    private int serverPort;

    private Scanner userInputScanner;

    public static void main(String[] args){

        //check to enter the corret name
        String readName = null;
        Scanner scan = new Scanner(System.in);
        System.out.println("Please input username:");
        while(readName == null || readName.trim().equals("")){
            // null, empty, whitespace(s) not allowed.
            readName = scan.nextLine();
            if(readName.trim().equals("")){
                System.out.println("Invalid. Please enter again:");
            }

            ///Code for duplicate name here
        }

        Client client = new Client(readName, myIP, portNumber);

        client.startClient(scan);
    }

    private Client(String userName, String ip, int portNumber){//structure to have a name, ip, port
        this.userName = userName;
        this.clientIP = ip;
        this.serverPort = portNumber;
    }

    private void startClient(Scanner scan){
        try{
            Socket socket = new Socket(clientIP, serverPort);
            Thread.sleep(1000); // waiting for network communicating.

            ServerThread serverThread = new ServerThread(socket, userName);//initiali ST
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();

            while(serverAccessThread.isAlive()){//check if thread is alive
                if(scan.hasNextLine()){//if scan has something to say WAIT for itzzz input
                    serverThread.addNextMessage(scan.nextLine());
                }
            }
        }catch(IOException ex){
            System.err.println("Fatal Connection error!");
            ex.printStackTrace();
        }catch(InterruptedException ex){
            System.out.println("Interrupted");
        }
    }
}