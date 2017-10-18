
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

    // The client socket
    private static Socket clientSocket = null;
    // The output stream
    private static PrintStream os = null;
    // The input stream
    private static BufferedReader is = null;

    private static BufferedReader inputLine = null;
    private static boolean closed = false;

    public static void main(String[] args) {

        // The  port.
        int portNumber = 2222;
        // The default host.
        String ip = "localhost";

        if (args.length < 2) {
            System.out.println("Usage: java MultiThreadChatClient <host> <portNumber>\n" + "Now using host=" + ip + ", portNumber=" + portNumber);
        } else {
            ip = args[0];
            portNumber = Integer.valueOf(args[1]).intValue();
        }

    // connect to serverrr and inizialize stuff
        try {
            clientSocket = new Socket(ip, portNumber);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            os = new PrintStream(clientSocket.getOutputStream());
            is = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("<<< SHIT SHIT SHIT whos the ip " + ip);
        } catch (IOException e) {
            System.err.println("<<< SHIT SHIT SHIT IO " + ip);
        }

        if (clientSocket != null && os != null && is != null) {
            try {

        //  a thread to read from  serve
                new Thread(new Client()).start();
                while (!closed) {
                    os.println(inputLine.readLine().trim());
                }
       //close  stuff
                os.close();
                is.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println(" <<< SHIT SHIT SHIT :  " + e);
            }
        }

//        Thread thread1 = new Thread() {
//            public void run() {
//                //   System.out.println("Thread is running");
//                int a = 0;
//                while (true) {
//                    PrintStream p = null;
//                    try {
//                        p = new PrintStream(clientSocket.getOutputStream());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    p.println("IMAV");
//                    try {
//                        Thread.sleep(60000); //
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                        System.out.println("Please restart app");
//                    }
//                }
//            }
//        };
//
//        thread1.start();
    }





    public void run() {
    //read until we receive the word "bye"
        String responseLine;
        try {
            while ((responseLine = is.readLine()) != null) {
                System.out.println(responseLine);
                if (responseLine.indexOf("*** Bye") != -1)
                    break;
            }
            closed = true;
        } catch (IOException e) {
            System.err.println("<<< SHIT SHIT SHIT:  " + e);
        }
    }
}