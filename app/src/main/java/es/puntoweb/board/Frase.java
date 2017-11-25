package es.puntoweb.board;

import android.content.res.AssetManager;
import android.os.SystemClock;

/**
 * Created by teo on 11/11/2017.
 */

public class  Frase extends Board {

    private static final  Frase INSTANCE=new Frase();
    private Frase(){}

    public static Frase getInstance(){
        return INSTANCE;
    }

    public  void play(AssetManager asset){
        int i=0;
        while (i<super.getElementSize()){
            super.getElement(i).playAudio(asset);
            SystemClock.sleep(1200);
            i++;
        }
    }




}
