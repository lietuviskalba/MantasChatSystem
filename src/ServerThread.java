import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class ServerThread implements Runnable {//needed for the treads to work
    private Socket socket;
    private String userName;
    private boolean isAlived;
    private final LinkedList<String> messagesToSend;
    private boolean hasMessages = false;

    public ServerThread(Socket socket, String userName){
        this.socket = socket;
        this.userName = userName;
        messagesToSend = new LinkedList<String>();
    }

    public void addNextMessage(String message){//receive the message
        synchronized (messagesToSend){//WAIT for the fcking thread to finsih work
            hasMessages = true;
            messagesToSend.push(message);//add the message received
        }
    }

    @Override
    public void run(){
        System.out.println("J_OK Protocol Welcome :" + userName);

        //System.out.println("Local Port :" + socket.getLocalPort());
        System.out.println("Server IP= " + socket.getInetAddress() + " PORT:" + socket.getPort());
        //System.out.println("Server = " + socket.getRemoteSocketAddress() + ":" + socket.getPort());

        try{
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverInStream = socket.getInputStream();
            Scanner serverIn = new Scanner(serverInStream);

            while(!socket.isClosed()){
                if(serverInStream.available() > 0){//bytes that can be red
                    if(serverIn.hasNextLine()){
                        System.out.println(serverIn.nextLine());
                    }
                }
                if(hasMessages){
                    String nextSend = "";


                    synchronized(messagesToSend){
                        nextSend = messagesToSend.pop();//get the messages from the list to display and remove it
                        hasMessages = !messagesToSend.isEmpty();//return not true when isEmpty

                    }

                    if(nextSend.equals("QUITE")){//My quite protocol
                        System.out.println("QUITE Protocol. Sad to see you go!");
                        socket.close();
                    }

                    if(nextSend.equals("LIST")){//My list protocol
                        System.out.println("List Protocol. See all the users!");
                        //code to display here


                         System.out.println(ChatServer.getNames());



                       // System.out.println(serverClients.getClients());


                    }

                    serverOut.println(userName + " ----> " + nextSend);
                    serverOut.flush();//poop


                }
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

    }
}