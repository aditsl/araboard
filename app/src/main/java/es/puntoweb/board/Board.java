package es.puntoweb.board;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by yosu on 05/11/2017.
 */

public class Board {

    private List<Element> elements;
    private String carpeta;
    private int filas,columnas;
    private boolean isAssets=true;

    Board(String carpeta){
        elements = new ArrayList<Element>();
        this.carpeta=carpeta;
    }

    Board(){
        elements = new ArrayList<Element>();
    }

    public void addElement(Element e){
        elements.add(e);

    }

    public void setFilas(int filas){
        this.filas=filas;

    }

    public int getFilas(){
        return this.filas;
    }

    public int getColumnas(){

        return this.columnas;
    }

    public void setColumnas(int columnas){
        this.columnas=columnas;
    }

    public Element getElement(int i){
        return  elements.get(i);
    }



    public int getElementSize(){
        return elements.size();
    }

    public String getCarpeta(){
        return this.carpeta;
    }

    public void delLastElement(){
        if (elements.size()>0) {
            this.elements.remove(elements.size() - 1);
        }

    }

    public void setIsAssets(Boolean bol){
        this.isAssets=bol;
    }

    public boolean getIsAssets(){
        return this.isAssets;
    }


}
