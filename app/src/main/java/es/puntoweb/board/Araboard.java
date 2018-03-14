package es.puntoweb.board;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;

/**
 * Created by teo on 13/11/2017.
 */

public class Araboard {

    public static final String IMGDIRNAME ="IMAGENES";
    public static final String AUDIODIRNAME ="AUDIOS";
    public static final String PATH = Environment.getExternalStorageDirectory() + "/araboard/";
    public static final String TMPBOARDPATH = PATH + "tmpboar/";
    public static final String TABLERO="tablero_comunicacion.xml";

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

    public static String getImgPath(String directory){
        return PATH+directory+File.separator+IMGDIRNAME+File.separator;
    }

    public static int getMenuRows(Context context){
        int rows=0;
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            rows=Integer.parseInt(SP.getString("menu_rows","0"));
        }catch(Exception e){

        }
        return rows;

    }

    public static int getMenuColums(Context context){
        int col=0;
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            col=Integer.parseInt(SP.getString("menu_columns","0"));
        }catch(Exception e){

        }
        return col;
    }


    public static int getBoardRows(Context context){
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(SP.getString("board_rows","0"));
    }

    public static int getBoardColums(Context context){
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(SP.getString("board_columns","0"));
    }

    public static boolean getBoardOverride(Context context){
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        return SP.getBoolean ("board_override",false);
    }

    public static String getAudioPath(String directory){
        return PATH+directory+File.separator+AUDIODIRNAME+File.separator;
    }

}
