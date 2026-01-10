/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.network;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.model.AuthResponseEntity;
import com.boredxgames.tictactoeclient.domain.services.communication.Message;
import com.boredxgames.tictactoeclient.domain.services.communication.MessageRouter;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Hazem
 */
public class ServerConnectionManager {

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Thread th;
    private Gson gson = new Gson();
    private AuthResponseEntity player;

    public void setPlayer(AuthResponseEntity player) {
        this.player = player;
    }
    private static ServerConnectionManager instance;

    private ServerConnectionManager() {

    }

    public static ServerConnectionManager getInstance() {
        if (instance == null) {
            instance = new ServerConnectionManager();
        }

        return instance;
    }

    public void connect(String host, int port) throws IOException {
        InetAddress ip = InetAddress.getByName("localhost");
        socket = new Socket(ip, port);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        th = new Thread(this::readMessages);
        th.start();

    }

    private void readMessages() {

        while (!socket.isClosed()) {
            try {
                String response = dis.readUTF();
                System.out.println(response);

                MessageRouter router = MessageRouter.getInstance();
                router.navigateMessage(response);
            } catch (IOException ex) {
                
                NavigationManager.navigate(Screens.PRIMARY, NavigationAction.REPLACE);
                close();
            }

        }

    }

    public synchronized void sendMessage(Message msg) {
        try {
            String jsonMessage = gson.toJson(msg);

            dos.writeUTF(jsonMessage);
            dos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
        } catch (IOException ex) {
            System.getLogger(ServerConnectionManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    public boolean isConnected() {
        return !socket.isClosed();
    }
}
