package es.puntoweb.board;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


public class BoardCreatorActivity extends Activity {

    private Board board=new Board();

    private static final int CAMERA_REQUEST = 1888;
    public static final int RECORD_REQUEST = 0;
    ImageButton playbtn,recbtn;
    int selectedbtn;
    int selectedelement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_board_creator);
        //Fill tmpboard directory whit subdirs
        AraboardParser.initializeDirectory();
        board.initializeElements();

        //Creating listeners for al Images,recordbuttons and Edittexts
        for (int x=1;x<5;x++) {
            for (int y=1;y<5;y++) {
                int imgid = getResources().getIdentifier("BCimg"+ x + y, "id", getApplicationContext().getPackageName());
                final int butonid=imgid;
                playbtn = (ImageButton) findViewById(imgid);
                playbtn.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                                   startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                                   selectedbtn= v.getId();
                                               }
                                           }
                );

                imgid = getResources().getIdentifier("BCimgr"+ x + y, "id", getApplicationContext().getPackageName());
                final int butonrid=imgid;
                recbtn = (ImageButton) findViewById(butonrid);
                recbtn.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  try {
                                                      Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                                                      startActivityForResult(intent, RECORD_REQUEST);
                                                      selectedelement = Board.getSelectedElement(v);

                                                  } catch (Exception e) {
                                                      Utils.log("Creating audio file");
                                                  }


                                              }
                                          }
                );
                imgid = getResources().getIdentifier("txtb"+ x + y, "id", getApplicationContext().getPackageName());
                final int edittxtid=imgid;
                EditText edit= (EditText) findViewById(edittxtid);
                edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                            @Override
                                            public void onFocusChange(View v, boolean hasFocus) {
                                               if (!hasFocus) {
                                                   int element=Board.getSelectedElement(v);
                                                   EditText texto=(EditText)v;
                                                   board.getElement(selectedelement).setTexto(texto.getText().toString());
                                                   Utils.log("Cambiado el foco"+texto.getText());
                                                }
                                            }
                                        }



                );


            }
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            //int imgid=(int) data.getExtras().getInt("button");
            playbtn = (ImageButton) findViewById(selectedbtn);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            playbtn.setImageBitmap(photo);


        }else if (requestCode==0 && resultCode==-1){
            board.getElement(selectedelement).setAudio(data.getDataString());
            Utils.log("Elemento seleccionado="+selectedelement);
        }
    }



}
