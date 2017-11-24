package com.example.yosu.board;
import android.util.Log;
import  	java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.res.AssetManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


/**
 * Created by yosu on 04/11/2017.
 */

public class AraboardParser {

    private  AssetManager assetManager;


    public  AraboardParser(InputStreamReader file, Board board ){
       BufferedReader reader = null;
       try {
           reader = new BufferedReader(file);
            //Initialize de parser
           XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
           factory.setNamespaceAware(true);
           XmlPullParser xpp = factory.newPullParser();
           xpp.setInput(reader);
           int eventType = xpp.getEventType();
           while (eventType != XmlPullParser.END_DOCUMENT) {
               if(eventType == XmlPullParser.START_DOCUMENT) {
                   Utils.log("Start document");
               } else if(eventType == XmlPullParser.END_DOCUMENT) {
                   Utils.log("End document");
               } else if(eventType == XmlPullParser.START_TAG) {
                   Utils.log("Start tag "+xpp.getName());
                   if (xpp.getName().equals("celda")){
                       Element celda=new Element(board.getCarpeta());
                       celda.setIsAssets(board.getIsAssets());
                       eventType=xpp.nextTag();
                       celda.setFila(Integer.parseInt(xpp.getAttributeValue(null, "fila")));
                       celda.setColumna(Integer.parseInt(xpp.getAttributeValue(null, "columna")));
                       eventType=xpp.nextTag();
                       eventType=xpp.nextTag();
                       eventType=xpp.nextTag();
                       eventType=xpp.nextTag();Utils.log("Imagen:"+ xpp.getAttributeValue(null, "imagen"));
                       celda.setImagen( xpp.getAttributeValue(null, "imagen"));
                       celda.setTexto(xpp.getAttributeValue(null, "texto"));
                       celda.setAudio(xpp.getAttributeValue(null, "audio"));
                       board.addElement(celda);
                   }else if (xpp.getName().equals("componentes")){

                      int filas=Integer.parseInt(xpp.getAttributeValue(null, "filas"));
                      int columnas=Integer.parseInt(xpp.getAttributeValue(null, "columnas"));
                      board.setFilas(filas);
                      board.setColumnas(columnas);
                   }
               } else if(eventType == XmlPullParser.END_TAG) {
                   Utils.log("End tag "+xpp.getName());
               } else if(eventType == XmlPullParser.TEXT) {
                   Utils.log("Text "+xpp.getText());
               }
               eventType = xpp.next();
           }
            // do reading, usually loop until end of file reading


            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                Utils.log(mLine);
            }
        } catch (IOException e) {
            Log.d("Yosu 36",e.getMessage());
        }catch (XmlPullParserException e){
            Utils.log(e.getMessage());

       }
       finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

}
