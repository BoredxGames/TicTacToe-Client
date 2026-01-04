/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.network;

import com.boredxgames.tictactoeclient.domain.services.communication.Message;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
/**
 *
 * @author Hazem
 */
public class ServerConnectionManager {
    private Socket socket; 
    private DataInputStream dis  ; 
    private DataOutputStream dos ;
    private Thread th ; 
    private static ServerConnectionManager instance ;
    
    private ServerConnectionManager() {
    }
  
    public synchronized ServerConnectionManager getInstance()
    {
        if(instance==null)
        {
            instance=new ServerConnectionManager();
        }
        
        return instance;
    }
    
    public void connect(String host , int port) throws IOException
    {
        socket = new Socket( host ,  port);
        dis= new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        th = new Thread(this::readMessages);
        th.start();
    }
    
    private void readMessages()
    {
       
            while (!socket.isClosed()) {
                try {
                    String jsonResponse = dis.readUTF();
                } catch (IOException ex) {
                    System.getLogger(ServerConnectionManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
                
            }
       
    }
    
    
    public synchronized void sendMessage(Message msg) throws IOException
    {
        String jsonMessage =null ;
        if(jsonMessage!=null)
        dos.writeUTF(jsonMessage.toString());
        dos.flush();
        
    }
    
    public void close (){
        try{
        if(socket!=null) {
            socket.close();
        }
        if(dis!=null) {
            dis.close();
        }
        if(dos!=null) {
            dos.close();
        }
    }
        catch (IOException ex) {
                System.getLogger(ServerConnectionManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        
       
    }
}
