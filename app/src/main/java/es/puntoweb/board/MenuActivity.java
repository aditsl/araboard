package es.puntoweb.board;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MenuActivity extends AppCompatActivity {

     String[][] boardData=new String[100][2];
     int sdStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Frase frase;
        frase=Frase.getInstance();
        AssetManager assetManager = getBaseContext().getAssets();
        listBoards(assetManager);
        cargaMenu();
    }


    private void cargaMenu(){
        Resources res=getResources();
        int numcolumnas=getLayoutColumns(true);
        int numfilas=getLayoutColumns(false);
        int x=1;
        //We create the table layout
        TableLayout table=findViewById(R.id.TablaInterna);
        TableRow tableRow;
        table.removeAllViews();
        TableLayout.LayoutParams layoutParamsMatch=new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutParamsBtn=new TableRow.LayoutParams((getWindowManager().getDefaultDisplay().getWidth()/numcolumnas)-10,(getWindowManager().getDefaultDisplay().getHeight()/numfilas)-40);
        for (int c=0;c<numfilas;c++) {
            tableRow = new TableRow(this);
            tableRow.setLayoutParams(layoutParamsMatch);
            for (int i = 0; i < numcolumnas; i++) {
                ImageButton btn = new ImageButton(this);
                TableRow.LayoutParams params = layoutParamsBtn;
                btn.setLayoutParams(layoutParamsBtn);
                btn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                tableRow.addView(btn);
                try {
                    InputStream bitmap;
                    if (x<sdStart) {
                        bitmap = getBaseContext().getAssets().open(boardData[x][0] + File.separator + Araboard.IMGDIRNAME + File.separator + boardData[x][1]);
                    }else{
                        File bmpfile=new File(Araboard.getImgPath(boardData[x][0])+boardData[x][1]);
                        bitmap = new FileInputStream(bmpfile);
                    }
                    Bitmap img= BitmapFactory.decodeStream(bitmap);
                    btn.setImageBitmap(img);
                    final int cual=x;
                    btn.setOnClickListener( new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v){
                                                    Intent i=new Intent(getBaseContext(),BoardActivity.class);
                                                    i.putExtra("BOARD",boardData[cual][0]);
                                                    startActivityForResult(i,1);
                                                }
                                            }
                    );


                 }catch (IOException e){

                 }
                 x++;
            }
            table.addView(tableRow);
            tableRow = new TableRow(this);
            for (int i = 0; i < numcolumnas; i++) {
                TextView txt = new TextView(this);
                txt.setText(boardData[x][0]);
                tableRow.addView(txt);

            }
            table.addView(tableRow);
        }


    }



    private  void listBoards(AssetManager assetManager){
       //Creates a list of boards presents on the system

        //First we load native app boards
        String[] dirNames={};
        try {
            dirNames = assetManager.list("");
            int cont=1;
            for (String name : dirNames) {

               String[] fileNames=assetManager.list(name);
                if (Arrays.asList(fileNames).contains("tablero_comunicacion.xml"))
                {
                    boardData[cont][0]=name;
                    String[] firstImage=assetManager.list(name+ File.separator+Araboard.IMGDIRNAME);
                    boardData[cont][1]=firstImage[0];
                    cont++;
                }


            }
            //Second we load boards presents in sdcard String PATH = Environment.getExternalStorageDirectory() + "/araboard/";
            sdStart=cont;
            File file = new File(Araboard.PATH);
            if (file.isDirectory()) {
                dirNames=file.list();
                for (String fi: dirNames){
                    File isdir=new File(Araboard.PATH+fi);
                    if (isdir.isDirectory()) {
                        boardData[cont][0]=fi;
                        File images=new File(Araboard.PATH+fi+File.separator+"IMAGENES");
                        String [] firstImage=images.list();
                        if (firstImage.length>0) {
                            boardData[cont][1] = firstImage[0];
                            cont++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Utils.log( e.getMessage());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==2){
            listBoards(getBaseContext().getAssets());
            cargaMenu();
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        }else{

        }
        cargaMenu();
    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
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
            Intent i=new Intent(getBaseContext(),SettingsActivity.class);
            startActivityForResult(i,1);
        }else if (id == R.id.action_download){
            Intent i=new Intent(getBaseContext(),DownloadActivity.class);
            startActivityForResult(i,2);
        }else if ( id == R.id.action_creator){
            Intent i=new Intent(getBaseContext(),BoardCreatorActivity.class);
            startActivityForResult(i,3);

        }
        return super.onOptionsItemSelected(item);
    }


    private int getLayoutColumns(Boolean bol){
        int files,columns=4;
        int screen_layout=getResources().getConfiguration().screenLayout;
        int display_mode = getResources().getConfiguration().orientation;

        //Check if columns, rows are set in preferences
        if ((Araboard.getMenuRows(getBaseContext())!=0) && !bol){
            return     Araboard.getMenuRows(getBaseContext());
        }
        if ((Araboard.getMenuColums(getBaseContext())!=0) && bol){
            return Araboard.getMenuColums(getBaseContext());
        }
        //Set default colums, rows diferent layouts
           if (( screen_layout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
               if (display_mode == Configuration.ORIENTATION_PORTRAIT) {
                   files=6;
                   columns=5;
               } else {
                   columns=6;
                   files=5;

               }
           }
           else if (( screen_layout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                if (display_mode == Configuration.ORIENTATION_PORTRAIT) {
                    files=5;
                    columns=5;
                } else {
                    columns=5;
                    files=5;

                }
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                if (display_mode == Configuration.ORIENTATION_PORTRAIT) {
                    files=4;
                    columns=3;
                } else {
                    columns=4;
                    files=3;
                }
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                files=4;
                columns=4;
            }
            else {
                files=4;
                columns=4;
            }

        if (bol)
           return columns;
        else
            return files;

    }
}
