package es.puntoweb.board;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;



import java.io.File;
import java.io.FileInputStream;
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
        int cont=0;
        int columna=1;
        int fila=1;
        for (int x=1; x<=boardData.length;x++){
                int imgid = res.getIdentifier("imageButton"+ fila + columna, "id", getApplicationContext().getPackageName());
                int txtid=res.getIdentifier("txt"+fila+columna,"id",getApplicationContext().getPackageName());
                ImageButton btn = findViewById(imgid);
                TextView txt = findViewById(txtid);
                final AssetManager assetManager = getBaseContext().getAssets();
                try {
                    InputStream bitmap;
                    if (x<sdStart) {
                        bitmap = getBaseContext().getAssets().open(boardData[x][0] + File.separator + Araboard.IMGDIR + File.separator + boardData[x][1]);
                    }else{
                        File bmpfile=new File(Araboard.PATH+boardData[x][0]+File.separator+Araboard.IMGDIR+ File.separator+boardData[x][1]);
                        bitmap = new FileInputStream(bmpfile);
                    }
                    Bitmap img= BitmapFactory.decodeStream(bitmap);
                    btn.setImageBitmap(img);
                    txt.setText(boardData[x][0]);
                    final int cual=x;
                    btn.setOnClickListener( new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v){
                                                    Intent i=new Intent(getBaseContext(),BoardActivity.class);
                                                    i.putExtra("BOARD",boardData[cual][0]);
                                                    startActivity(i);
                                                }
                                            }
                    );
                    cont++;
                    if(fila%4==0)
                    {
                        fila=1;
                        columna++;
                    }else{
                        fila++;
                    }
                } catch (Exception e) {
                    Utils.log( e.getMessage());
                }
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
                    String[] firstImage=assetManager.list(name+ File.separator+Araboard.IMGDIR);
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
                        boardData[cont][1]=firstImage[0];
                        cont++;
                    }
                }
            }
        } catch (Exception e) {
            Utils.log( e.getMessage());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Utils.log("Codigo="+requestCode);
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
            return true;
        }else if (id == R.id.action_download){
            Intent i=new Intent(getBaseContext(),DownloadActivity.class);
            startActivity(i);
        }else if ( id == R.id.action_creator){
            Intent i=new Intent(getBaseContext(),BoardCreatorActivity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }
}
