/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.services.communication;

/**
 *
 * @author Hazem
 */

import com.boredxgames.tictactoeclient.domain.network.ServerConnectionManager;
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.LOGIN;
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.REGISTER;

public class MessageRouter {
    private static MessageRouter instance;
    private static ServerConnectionManager connection;
    private MessageRouter() {
        connection = ServerConnectionManager.getInstance();
    }

    public static MessageRouter getInstance() {
        if (instance == null) {
            instance = new MessageRouter();
        }
        return instance;
    }

    public void navigateMessage(Message message){
       MessageType messageType = message.getHeader().getMsgType();
       
       assert messageType!=null; 
       Message responseMessage;
        switch (messageType) {
            case REQUEST:
                 responseMessage = handleRequest(message);
                break;
            case RESPONSE:
                 responseMessage = handleResponse(message);
                break;
            case EVENT:
                // Handle event messages
                break;
            case ERROR:
                // Handle error messages
                break;
            default:
                System.out.println("Unknown MessageType: " + messageType);
                break;
        }
    }

    private Message handleRequest(Message msg) {
        Action action = msg.getHeader().getAction();

        assert action != null;
        return switch (action) {
            case LOGIN :
            case REGISTER :
            default : {
                System.out.println("Unknown Action: " + action);
                yield new Message();
            }
        };
    }
    
    private Message handleResponse(Message msg) {
        Action action = msg.getHeader().getAction();

        assert action != null;
        return switch (action) {
            case LOGIN :
            case REGISTER :
            default : {
                System.out.println("Unknown Action: " + action);
                yield new Message();
            }
        };
    }


}
