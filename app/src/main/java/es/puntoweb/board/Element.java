
package es.puntoweb.board;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import java.io.File;
import java.io.FileInputStream;
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
    private boolean isAssets;

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
            Bitmap img;
             if (isAssets) {
                 InputStream bitmap = assetManager.open(directorio + File.separator + this.imagen);
                 img = BitmapFactory.decodeStream(bitmap);
             }else{
                 File bmpfile=new File(Araboard.PATH+directorio+File.separator+imagen);
                 FileInputStream bitmap = new FileInputStream(bmpfile);
                 img= BitmapFactory.decodeStream(bitmap);
             }
             return img;
    }

    public String getAudio(AssetManager assetManager) throws  IOException{
        return this.audio;
    }

    public  void playAudio(AssetManager assetManager) {
        String ruta="";
        MediaPlayer mp=new MediaPlayer();
        try{
            if (isAssets) {
                ruta = directorio + File.separator + this.audio;
                AssetFileDescriptor fd=assetManager.openFd(ruta);
                mp.setDataSource(fd.getFileDescriptor(),fd.getStartOffset(),fd.getLength());
            }else{
                ruta=Araboard.PATH+directorio+ File.separator+audio;
                mp.setDataSource(ruta);
            }

          mp.prepare();
          mp.start();
            Utils.log( "playAudio");
        }catch (Exception e){
            Utils.log( "Imposible encontrar el audio "+ruta);

        }


    }

    public void setIsAssets(Boolean bol){
        this.isAssets=bol;
    }
}
