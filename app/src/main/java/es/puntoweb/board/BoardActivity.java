package es.puntoweb.board;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.AssetManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class BoardActivity extends AppCompatActivity {

    private String folderName="ACCIONES";
    Board matriz;
    Frase frase;
    private int page=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        InputStreamReader tablero;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);
        String folderName=getIntent().getStringExtra("BOARD");
        AssetManager assetManager = getBaseContext().getAssets();
        matriz=new Board(folderName);
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
        //loadBoard(matriz);
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

        int numcolumnas=getLayoutColumns(true);
        int numfilas=getLayoutColumns(false);
        int cont=0;
        if (page>0) {
            cont =(page * numcolumnas * numfilas)-page;
        }
        Resources res=getResources();
        //We create the table layout
        TableLayout table=findViewById(R.id.tableBoard);
        TableRow tableRow;
        table.removeAllViews();
        TableLayout.LayoutParams layoutParamsMatch=new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutParamsBtn=new TableRow.LayoutParams((getWindowManager().getDefaultDisplay().getWidth()/numcolumnas)-10,(getAvailableHeight(numfilas)/numfilas));
        for (int c=1; c<=numfilas;c++){
            tableRow = new TableRow(this);
            tableRow.setLayoutParams(layoutParamsMatch);
            for (int i=1; i<=numcolumnas;i++) {
                ImageButton btn = new ImageButton(this);
                TableRow.LayoutParams params = layoutParamsBtn;
                btn.setLayoutParams(layoutParamsBtn);
                btn.setBackgroundColor(Color.WHITE);
                btn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                btn.setBackground(getResources().getDrawable(R.drawable.ripple_effect));
                //Is we have a custom Board matrix whe have to insert show next elements button
                if (Araboard.getBoardOverride(getApplicationContext())) {
                    if ((page*numfilas*numcolumnas)+(numcolumnas * numfilas) - (page+1) == cont) {
                        btn.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                page++;
                                loadBoard(matriz);
                            }
                        });
                        tableRow.addView(btn);
                        break;
                    }
                }
                tableRow.addView(btn);
                final AssetManager assetManager = getBaseContext().getAssets();
                try {
                    //Some Araboard tableros has empty cells!
                    if (board.getElement(cont).getTexto()!=null) {
                        btn.setImageBitmap(board.getElement(cont).getImagen(getAssets()));
                        //txt.setText(board.getElement(cont).getTexto());
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
            table.addView(tableRow);
            tableRow = new TableRow(this);
            TableRow.LayoutParams layoutParamsTxt=new TableRow.LayoutParams((getWindowManager().getDefaultDisplay().getWidth()/numcolumnas)-10,dpToPx(20));
            tableRow.setLayoutParams(layoutParamsTxt);
            int start=(page*numcolumnas*numfilas)+(c*numcolumnas)-numcolumnas-page;
            int end=(page*numcolumnas*numfilas)+c*numcolumnas+numfilas;
            for (int i = start; i < end; i++) {
                TextView txt = new TextView(this);
                txt.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                txt.setLayoutParams(layoutParamsTxt);
                //More columns than Board
                if (board.getElementSize()<=i){
                    txt.setText("");
                }else {
                    //Inser next slide button if it is last icon of the page
                    if (Araboard.getBoardOverride(getApplicationContext())) {
                        if ( i+1==numcolumnas*numfilas*page+(numcolumnas*numfilas)-page) {
                            txt.setText(getText(R.string.next));
                        }else{
                            txt.setText(board.getElement(i).getTexto());
                        }
                    }else {
                        txt.setText(board.getElement(i).getTexto());
                    }
                }
                //txt.setHeight(dpToPx(20));
                tableRow.addView(txt);

            }
            table.addView(tableRow);
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


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        loadBoard(matriz);
    }

    private int getLayoutColumns(Boolean bol) {
        //Check if columns, rows are set in preferences
        if (Araboard.getBoardOverride(getBaseContext())){
            if ((Araboard.getBoardRows(getBaseContext()) != 0) && !bol) {
                return Araboard.getBoardRows(getBaseContext());
            }
            if ((Araboard.getBoardColums(getBaseContext()) != 0) && bol) {
                return Araboard.getBoardColums(getBaseContext());
            }
        }
        //Set default colums, rows diferent layouts
        if (bol)
            return matriz.getColumnas();
        else
            return matriz.getFilas();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadBoard(matriz);

    }

    private int getAvailableHeight(int filas){
        TableRow table=findViewById(R.id.rowFrase);
        return getWindowManager().getDefaultDisplay().getHeight()-getSoftButtonsBarHeight()-getStatusBarHeight()-(dpToPx(20)*filas);

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private int getSoftButtonsBarHeight() {
        Resources resources = getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private  int dpToPx(int dp) {
        float density = getBaseContext().getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
