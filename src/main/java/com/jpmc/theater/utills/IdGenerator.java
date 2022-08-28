package com.jpmc.theater.utills;

import java.util.Map;

public class IdGenerator {

    public static String nextId(Map map, String prefix){
        return prefix+(map.size()+1);
    }

}
