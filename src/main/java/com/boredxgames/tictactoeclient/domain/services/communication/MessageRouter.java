/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.services.communication;

/**
 *
 * @author Hazem
 */

import org.json.JSONObject;

public class MessageRouter {
    private static MessageRouter instance;

    private MessageRouter() {
    }

    public static MessageRouter getInstance() {
        if (instance == null) {
            instance = new MessageRouter();
        }
        return instance;
    }

    public void navigateMessage(String message)  {
       
    }

    private Message handleRequest(JSONObject json) {
        Action action = Action.valueOf(json.getInt("action"));

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
