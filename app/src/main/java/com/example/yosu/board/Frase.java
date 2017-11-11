package com.example.yosu.board;

/**
 * Created by teo on 11/11/2017.
 */

public class  Frase extends Board {

    private static final  Frase INSTANCE=new Frase();
    private Frase(){}

    public static Frase getInstance(){
        return INSTANCE;
    }


}
