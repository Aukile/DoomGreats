package com.ankrya.doomsgreats.data;

public class ModVariable {
    public static final String test = "test";
    public static void init(Variables variables){
        variables.registerVariable(int.class, test, 0, false);
    }
}
