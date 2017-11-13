package com.example.yosu.board;

/**
 * Created by teo on 13/11/2017.
 */

public class Araboard {

    private static Araboard INSTANCE=new Araboard();

    private Araboard(){}



    public static Araboard getInstance(){
        return INSTANCE;
    }
}
