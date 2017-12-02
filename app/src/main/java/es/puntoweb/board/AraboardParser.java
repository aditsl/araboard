package es.puntoweb.board;
import java.io.BufferedReader;
import java.io.IOException;
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
                       eventType=xpp.nextTag();
                       celda.setImagen(xpp.getAttributeValue(null, "imagen"));
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
           Utils.log(e.getMessage());
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

    public static String parseBoard(Board board){
        String xml;
        xml="<xml version=\"1.0\" encoding=\"ISO-8859-1\">\n" +
                "  <cabecera>\n" +
                "    <fondo color=\"16187287\"/>\n" +
                "    <letra color=\"65793\"/>\n" +
                "    <nombre titulo=\" "+board.getCarpeta() +". ARABOARD\"/>\n" +
                "    <componentes filas=\"4\" columnas=\"7\"/>\n" +
                "  </cabecera>";
        int i=0;
        while (i<board.getElementSize()){
            xml+="<celda>\n" +
                    "    <coordenadas fila=\"1\" columna=\"1\"/>\n" +
                    "    <ocupada flag=\"true\"/>\n" +
                    "    <pictograma imagen=\"IMAGENES/23392.png\" audio=\"AUDIOS/0/14.mp3\" texto=\"JUGAR\" categoria=\"3\" color=\"3381504\"/>\n" +
                    "  </celda>";
            i++;
        }
        xml+="</xml>";
        return xml;
   }

    public static void initializeDirectory(){
        Utils.makeDirIfNotExist(Araboard.PATH);
        Utils.makeDirIfNotExist(Araboard.TMPBOARDPATH);
        Utils.deleteDirRecursive(Araboard.TMPBOARDPATH);
        Utils.makeDirIfNotExist(Araboard.TMPBOARDPATH+Araboard.IMGDIRNAME);
        Utils.makeDirIfNotExist(Araboard.TMPBOARDPATH+Araboard.AUDIODIRNAME);
    }


}
