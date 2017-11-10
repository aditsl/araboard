package com.example.yosu.board;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.AssetManager;
import android.widget.ImageButton;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class BoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        AssetManager assetManager = getBaseContext().getAssets();
        Board matriz=new Board("ACCIONES");
        AraboardParser parser=new AraboardParser(assetManager, matriz);
        cargaBoard(matriz);

    }

    private void cargaBoard(Board board){
        Resources res=getResources();
        int cont=0;
        for (int fila=1; fila<=board.getFilas();fila++){
            for (int columna=1; columna<=board.getColumnas();columna++) {
                int imgid = res.getIdentifier("imageButton"+ fila + columna, "id", getApplicationContext().getPackageName());
                ImageButton btn = findViewById(imgid);
                final AssetManager assetManager = getBaseContext().getAssets();
                try {
                    btn.setImageBitmap(board.getElement(cont).getImagen(getAssets()));

                    final Element element=board.getElement(cont);
                    btn.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v){
                                element.playAudio(assetManager);
                            }
                    }
                    );
                    cont++;
                } catch (Exception e) {
                    Log.d("Yosu", e.getMessage());
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
