package com.example.yosu.board;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;

public class BoardCreatorActivity extends Activity {

    private static final int CAMERA_REQUEST = 1888;
    ImageButton playbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_board_creator);
        for (int x=1;x<4;x++) {
            for (int y=1;y<4;y++) {
                int imgid = getResources().getIdentifier("BCimg"+ x + y, "id", getApplicationContext().getPackageName());
                playbtn = (ImageButton) findViewById(imgid);
                playbtn.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                                   startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                               }
                                           }
                );
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            playbtn.setImageBitmap(photo);
        }
    }



}
