package com.example.yosu.board;

import android.os.Environment;

import java.io.File;

/**
 * Created by teo on 13/11/2017.
 */

public class Araboard {

    public static final String IMGDIR="IMAGENES";
    public static final String PATH = Environment.getExternalStorageDirectory() + "/araboard/";
    public static final String TMPBOARDPATH = PATH + "tmpboar/";



    private static Araboard INSTANCE=new Araboard();


    private Araboard(){}



    public static Araboard getInstance(){
        return INSTANCE;
    }

    public static File getTmpAudioFile(){
        File file=NewAraFile(TMPBOARDPATH,"audio.3gp");
        return file;

    }

    public static File NewAraFile(String Path,String name){
        File file = new File(Path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File outputFile = new File(file, name);
        return outputFile;
    }


}
