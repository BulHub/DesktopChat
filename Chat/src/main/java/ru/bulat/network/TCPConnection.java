package ru.bulat.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConnection {
    private final Socket socket;
    private final Thread rxThread;
    private final TCPConnectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TCPConnection(TCPConnectionListener eventListener, String ipAddress, int port) throws IOException {
        this(eventListener, new Socket(ipAddress, port));

    }

    public TCPConnection(final TCPConnectionListener eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCPConnection.this);
                    while (!rxThread.isInterrupted()){
                        eventListener.onReceiveString(TCPConnection.this, in.readLine());
                    }
                } catch (IOException ex) {
                    eventListener.onException(TCPConnection.this, ex);
                } finally {
                    eventListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        rxThread.start();
    }

    public synchronized void sendString(String value){
        try {
            out.write(value + "\r\n");
            out.flush();
        }catch (IOException ex){
            eventListener.onException(TCPConnection.this, ex);
            disconnect();
        }
    }

    private synchronized void disconnect(){
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException ex) {
            eventListener.onException(TCPConnection.this, ex);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}
