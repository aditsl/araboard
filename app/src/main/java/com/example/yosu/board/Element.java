
package com.example.yosu.board;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
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
    private String directorio;

    public void setFila(int fila){
        this.fila=fila;
    }

    public Element(String directorio){
        this.directorio=directorio;
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
    public String getTexto() {return this.texto; }

    public Bitmap getImagen(AssetManager assetManager) throws  IOException{
             InputStream bitmap=assetManager.open(directorio + File.separator +this.imagen);
             Bitmap img= BitmapFactory.decodeStream(bitmap);
             return img;
    }

    public String getAudio(AssetManager assetManager) throws  IOException{
        return this.audio;
    }

    public  void playAudio(AssetManager assetManager) {
        String ruta=directorio + File.separator +this.audio;
        try{

          AssetFileDescriptor fd=assetManager.openFd(ruta);
          MediaPlayer mp=new MediaPlayer();
          mp.setDataSource(fd);
          mp.prepare();
          mp.start();
            Utils.log( "playAudio");
        }catch (Exception e){
            Utils.log( "Imposible encontrar el audio "+ruta);

        }


    }
}
