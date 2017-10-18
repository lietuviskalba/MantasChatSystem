
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;


public class Server {

    // The server socket.
    private static ServerSocket serverSocket = null;
    // The client socket.
    private static Socket clientSocket = null;

    // This chat server has a client CAP
    private static final int maxClientsCount = 20;
    private static final clientThread[] threads = new clientThread[maxClientsCount];

     static final int portNumber = 2222;
     static final String IP = " <<< IP HERE PLEASE";

    public static void main(String args[]) {

        // The default port number.


        if (args.length < 1) {
            System.out.println(" Greetings to the chat server \n" + "Start the JOIN protocol");
        } else {
            //portNumber = Integer.valueOf(args[0]).intValue();
        }

    //SERVER SOCKET HERHERHE
        try {
            serverSocket = new ServerSocket(portNumber); // add IP HERE
        } catch (IOException e) {
            System.out.println(e);
        }

    // accepts client sockets

    //add a new client to thread
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                int i = 0;
                for (i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new clientThread(clientSocket, threads)).start();
                        break;
                    }
                }
                //check that you dont breach the limit
                if (i == maxClientsCount) {
                    PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    os.println("Server too busy. Try later.");
                    os.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}


class clientThread extends Thread {

    PeopleData pd = new PeopleData();

    private String clientName = null;
    private BufferedReader is = null;
    private PrintStream os = null;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;

    public clientThread(Socket clientSocket, clientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }

    /// LOL STUFF

//    public static List<String> userNames = new ArrayList<>();
//
//    public  void addName(String enteredName){
//        userNames.add(enteredName);
//    }
//
//    public  void displayUsers(){
//        for (String name : userNames){
//            System.out.println("Ze names: "+name);
//        }
//    }
//
//    public  List<String> getUserNames(){
//
//        return userNames;
//    }
    /// LOL STUFF

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;

        try {

            // Create input and output streams for this client.

            is = new BufferedReader( new InputStreamReader (clientSocket.getInputStream()));//get input from clients
            os = new PrintStream(clientSocket.getOutputStream());//send out to clients
            String name="";

            boolean isNameDup = false;

            //Enter correct name
            while (true) {
                os.println("Enter your name.");

                    name = is.readLine().trim();

                if(pd.getUserNames().size() >=1){

                    for(int i=0; i<pd.getUserNames().size();i++){
                        //System.out.println(" mes siekiam sita");
                        if(name.equals(pd.getUserNames().get(i))){
                           // System.out.println(" mes siekiam sita irgi !!!!!!!");
                            os.println("J_ER: name DUPLICATES");
                            isNameDup = true;
                        }else{
                            isNameDup = false;
                        }
                    }
                }

                    if (name.indexOf('@') == -1 && name.length() <= 12 && isNameDup == false) {

                        pd.addName(name);

                        break;
                    } else{
                        os.println("J_ER: name too long or bad symbols");
                    }
            }

//            while (true) {
//                os.println("Enter your IP.");
//                String Ip = is.readLine().trim();
//                os.println("Enter your PORT.");
//                int PORT = is.read();
//                break;
//            }

      // Welcome the new the client

            os.println("J_OK: Welcome <<<" + name  + ">>> to chat room.\nTo leave enter QUIT in a new line.");
            System.out.println(name + " joined the server");
            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && threads[i] == this) {
                        clientName = "###" + name;
                        break;
                    }
                }
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && threads[i] != this) {
                        threads[i].os.println("*** A new user <<<" + name + ">>> joined ***");
                    }
                }
            }
      /* Start the conversation. */
            while (true) {
                String line = is.readLine();

                if (line.startsWith("QUIT")) {
                    // remove from list as wel
                    pd.getUserNames().remove(pd.getUserNames().size()-1);
                    break;
                   // clientSocket.close();
                }
                if (line.startsWith("LIST")) {
                    os.println("You requested the list for all users: ");
                    System.out.println(clientName + " want the user LIST");
                    //PeopleData pd = new PeopleData();
                    //displayUsers();
                    os.println(pd.getUserNames());
                    System.out.println(pd.getUserNames());
//                        for (int i = 0; i < maxClientsCount; i++) {
//                            os.println(clientName);
//                        }
                }

          // TThis is where the messaging sends off
                    synchronized (this) {
                        for (int i = 0; i < maxClientsCount; i++) {
                            if (threads[i] != null && threads[i].clientName != null) {
                                threads[i].os.println("<" + name + ">: " + line);
                            }
                        }
                    }
            }
            //show who DCed
            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && threads[i] != this
                            && threads[i].clientName != null) {
                        threads[i].os.println("*** The user <<<" + name + ">>> left ***");
                    }
                }
            }
            os.println("*** Bye stinky, " + name + " ***");

      //reset tread after the left user
            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == this) {
                        threads[i] = null;
                    }
                }
            }
      //close the shit
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {
        }
    }
}