package es.puntoweb.board;

import android.view.View;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;


/**
 * Created by yosu on 05/11/2017.
 */

public class Board implements  Iterable<Element>{

    private List<Element> elements;
    private String carpeta;
    private int filas,columnas;
    private boolean isAssets=true;

    Board(String carpeta){
        elements = new ArrayList<Element>();
        this.carpeta=carpeta;
    }

    @Override
    public Iterator<Element> iterator() {
        return new Iterator<Element> () {
            private final Iterator<Element> iter = elements.iterator();

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Element next() {
                return iter.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("no changes allowed");
            }
        };
    }


    Board(){
        elements = new ArrayList<Element>();
    }

    public void initializeElements(){
        int i=0;
        while (i<16){
            Element elemento=new Element();
            this.addElement(elemento);
            i++;
        }
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

    public static  int getSelectedElement(View v){
        String ID = v.getResources().getResourceName(v.getId());
        int selectedfile=  Integer.parseInt(ID.substring(ID.length()-2,ID.length()-1));
        int selectedcolumn=Integer.parseInt(ID.substring(ID.length()-1,ID.length()));
        return 4*(selectedfile-1)+selectedcolumn;
    }
}
