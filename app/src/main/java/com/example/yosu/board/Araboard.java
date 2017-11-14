package com.example.yosu.board;

import android.os.Environment;

/**
 * Created by teo on 13/11/2017.
 */

public class Araboard {

    public static final String IMGDIR="IMAGENES";
    public static final String PATH = Environment.getExternalStorageDirectory() + "/araboard/";



    private static Araboard INSTANCE=new Araboard();


    private Araboard(){}



    public static Araboard getInstance(){
        return INSTANCE;
    }


}
