package com.expr.IRCMessage;

import com.expr.IRCMessage.Message;
import com.expr.IRCMessage.ParseException;
import java.util.*;

class Parse {
    public static Message parse(String line) {
        Message message = new Message();
        int position = 0;
        int nextspace = 0;
        // parsing!
        if (line.charAt(0) == '@') {
            String[] rawTags;
            
            nextspace = line.indexOf(" ");
            System.out.println(nextspace);
            if (nextspace == -1) {
                return null;
            }
            
            rawTags = line.substring(1, nextspace).split(";");
            
            for (int i = 0; i < rawTags.length; i++) {
                String tag = rawTags[i];
                String[] pair = tag.split("=");
                
                if (pair.length == 2) {
                    message.tags.put(pair[0], pair[1]);
                } else {
                    message.tags.put(pair[0], true);
                }
            }
            position = nextspace + 1;
        }
        
        while (line.charAt(position) == ' ') {
            position++;
        }
        
        if (line.charAt(position) == ':') {
            nextspace = line.indexOf(" ", position);
            if (nextspace == -1) {
                return null;
            }
            message.prefix = line.substring(position + 1, nextspace);
            position = nextspace + 1;
            
            while (line.charAt(position) == ' ') {
                position++;
            }
        }
        
        nextspace = line.indexOf(" ", position);
        
        if (nextspace == -1) {
            if (line.length() > position) {
                message.command = line.substring(position);
            }
            return message;
        }
        
        message.command = line.substring(position, nextspace);
        
        position = nextspace + 1;
        
        while (line.charAt(position) == ' ') {
            position++;
        }
        
        while (position < line.length()) {
            nextspace = line.indexOf(" ", position);
            
            if (line.charAt(position) == ':') {
                String param = line.substring(position + 1);
                message.params.add(param);
                break;
            }
            
            if (nextspace != -1) {
                String param = line.substring(position, nextspace);
                message.params.add(param);
                position = nextspace + 1;
                
                while (line.charAt(position) == ' ') {
                    position++;
                }
                continue;
            }
            
            if (nextspace == -1) {
                String param = line.substring(position);
                message.params.add(param);
                break;
            }
        }
        
        return message;
    }
}