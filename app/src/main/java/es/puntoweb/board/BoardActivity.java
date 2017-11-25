package es.puntoweb.board;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.AssetManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class BoardActivity extends AppCompatActivity {

    private String folderName="ACCIONES";
    Frase frase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        InputStreamReader tablero;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);
        String folderName=getIntent().getStringExtra("BOARD");
        AssetManager assetManager = getBaseContext().getAssets();
        Board matriz=new Board(folderName);
        frase=Frase.getInstance();
        tablero=null;
        //We have to see if the board has ben downloaded or is an asset
        try {
            if (Utils.fileExist(Araboard.PATH + folderName + File.separator + Araboard.TABLERO)) {
                FileInputStream tablerof = new FileInputStream(Araboard.PATH + folderName + File.separator + Araboard.TABLERO);
                tablero = new InputStreamReader(tablerof);
                matriz.setIsAssets(false);
            } else {
                tablero = new InputStreamReader(assetManager.open(folderName + File.separator + Araboard.TABLERO), "ISO-8859-1");
                matriz.setIsAssets(true);
            }
        }catch(Exception e)
        {

        }
        AraboardParser parser=new AraboardParser(tablero, matriz);
        loadBoard(matriz);
        ImageButton  playbtn = (ImageButton) findViewById(R.id.playButton);
        playbtn.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                       frase.play(getAssets());
                                       }
                                   }
        );
        ImageButton delbtn=(ImageButton) findViewById(R.id.deleteButton);
        delbtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            frase.delLastElement();
                                            loadPhrase(frase);
                                        }
            }
        );
        loadPhrase(frase);
    }

    private void loadPhrase(Frase frase){
        int i=0;
        LinearLayout ll=findViewById(R.id.Frase);
        ll.removeAllViews();
        while (i<frase.getElementSize()){
            final Element element=frase.getElement(i);
            try {
                ImageButton btnNew=new ImageButton(this);
                btnNew.setImageBitmap(element.getImagen(getAssets()));
                btnNew.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                btnNew.setAdjustViewBounds(true);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                btnNew.setLayoutParams(layoutParams);
                ll.addView(btnNew);
            }catch (Exception e){
               Utils.log(" ERROR Board showing phrase " + e.getMessage());
            }
            i++;
        }

    }


    private void loadBoard(Board board){
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
                    //Some Araboard tableros has empty cells!
                    if (board.getElement(cont).getTexto()!=null) {
                        btn.setImageBitmap(board.getElement(cont).getImagen(getAssets()));
                        txt.setText(board.getElement(cont).getTexto());
                    }
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
                    Utils.log(e.getMessage());
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
