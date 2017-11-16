package com.example.yosu.board;

import android.app.ActionBar;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class BoardActivity extends AppCompatActivity {

    private String folderName="ACCIONES";
    Frase frase;

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
        String folderName=getIntent().getStringExtra("BOARD");
        AssetManager assetManager = getBaseContext().getAssets();
        Board matriz=new Board(folderName);
        frase=Frase.getInstance();
        AraboardParser parser=new AraboardParser(assetManager, matriz);
        cargaBoard(matriz);
        ImageButton  playbtn = (ImageButton) findViewById(R.id.playButton);
        playbtn.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                                frase.play(getAssets());
                                       }
                                   }


        );



    }

    private void cargaBoard(Board board){
        Resources res=getResources();
        int cont=0;
        for (int fila=1; fila<=4;fila++){
            for (int columna=1; columna<=4;columna++) {
                int imgid = res.getIdentifier("imageButton"+ fila + columna, "id", getApplicationContext().getPackageName());
                int txtid = res.getIdentifier("txtb"+ fila + columna, "id", getApplicationContext().getPackageName());
                ImageButton btn = findViewById(imgid);
                TextView txt = findViewById(txtid);
                final AssetManager assetManager = getBaseContext().getAssets();
                try {
                    btn.setImageBitmap(board.getElement(cont).getImagen(getAssets()));
                    txt.setText(board.getElement(cont).getTexto());
                    final BoardActivity bA=this;
                    final Element element=board.getElement(cont);
                    btn.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v){
                                element.playAudio(assetManager);
                                frase.addElement(element);
                                LinearLayout ll=findViewById(R.id.Frase);
                                ImageButton btnNew=new ImageButton(bA);
                                try {
                                    btnNew.setImageBitmap(element.getImagen(getAssets()));
                                    btnNew.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                    btnNew.setAdjustViewBounds(true);
                                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                                    btnNew.setLayoutParams(layoutParams);
                                    ll.addView(btnNew);

                                }catch (Exception e){

                                }
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
