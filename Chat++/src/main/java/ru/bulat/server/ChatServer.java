package ru.bulat.server;

import ru.bulat.network.TCPConnection;
import ru.bulat.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListener {
    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    public static void main(String[] args) {
        new ChatServer();
    }

    private ChatServer(){
        System.out.println("Server running ...");
        try (ServerSocket serverSocket = new ServerSocket(8000)){
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                }catch (IOException ex){
                    System.out.println("TCPConnection exception: " + ex);
                }
            }
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendAllConnections("Client connection: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendAllConnections("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception ex) {
        System.out.println("TCPConnection exception: " + ex);
    }

    private void sendAllConnections(String value){
        System.out.println(value);
        for (TCPConnection connection : connections) connection.sendString(value);
    }
}
