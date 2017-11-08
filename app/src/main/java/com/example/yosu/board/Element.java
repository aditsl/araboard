
package com.example.yosu.board;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yosu on 05/11/2017.
 */

public class Element {

    private int fila;
    private int columna;
    private String imagen;
    private String audio;
    private String texto;

    public void setFila(int fila){
        this.fila=fila;
    }

    public void setColumna(int columna){
        this.columna=columna;
    }

    public void setImagen(String imagen){
        this.imagen=imagen;

    }
    public void setAudio(String audio){
        this.audio=audio;
    }
    public void setTexto(String texto){
        this.texto=texto;
    }

    public Bitmap getImagen(AssetManager assetManager) throws  IOException{

             InputStream bitmap=assetManager.open("SENYALA_VERDURAS" + File.separator +this.imagen);
             Bitmap img= BitmapFactory.decodeStream(bitmap);
             return img;

    }

}
