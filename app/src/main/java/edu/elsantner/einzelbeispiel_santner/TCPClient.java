package edu.elsantner.einzelbeispiel_santner;

import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class TCPClient {
    // TCP Server Address
    private static final String IP = "se2-isys.aau.at";
    private static final int PORT = 53212;

    private PrintWriter bufferOut;
    private BufferedReader bufferIn;

    public Observable<String> send(final String msg) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() {
                try {
                    String receivedString;
                    // init server connection
                    InetAddress serverIP = InetAddress.getByName(IP);
                    try (Socket socket = new Socket(serverIP, PORT)) {
                        //init in and out streams
                        bufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                        bufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        // send mnr to server and ...
                        bufferOut.println(msg);
                        // ... receive response from server
                        receivedString = bufferIn.readLine();
                    }
                    return receivedString;
                }
                catch (Exception ex) {
                    Log.e("TCPClient", "Error", ex);
                    return "Error: " + ex.getMessage();
                }
            }
        });
    }
}
