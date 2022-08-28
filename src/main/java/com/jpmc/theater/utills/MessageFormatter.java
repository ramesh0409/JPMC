package com.jpmc.theater.utills;

public class MessageFormatter {

    public static String plural(String message,int value){
        return value+" "+ (value>1?message+"s":message);
    }
}
