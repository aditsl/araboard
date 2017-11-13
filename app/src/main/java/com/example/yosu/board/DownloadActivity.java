package com.example.yosu.board;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadActivity extends AppCompatActivity {

    Button btnDownload;
    TextView txtUrl;

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    public String app_name="board" ;
    public String urlpath ;

    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_download);
        btnDownload=findViewById(R.id.btnDownload);
        txtUrl=findViewById(R.id.txtUrl);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download();
            }
        });
    }

    public void download() {
        urlpath = txtUrl.getText().toString();
        Log.d("Yosu", "Descargo " + urlpath);
        if (urlpath.length() == 0) {
            showAlert(getString(R.string.empty_url), getString(R.string.fill_url));

        }
        if (!Utils.checkURL(urlpath)) {
            showAlert(getString(R.string.wrong_url), getString(R.string.fill_url));
            return;
        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};
                int permsRequestCode = 200;
                requestPermissions(perms, permsRequestCode);

            } else {
                new DownloadFileAsync().execute(urlpath);

            }

        }
    }




    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                boolean writeAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                new DownloadFileAsync().execute(urlpath);
                break;

        }

    }




    private void showAlert(String title, String subject)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(subject);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();


    }



    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Downloading Updates..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMax(100);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }


    class DownloadFileAsync extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {


                URL url = new URL(urlpath.toString()); // Your given URL.

                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect(); // Connection Complete here.!


                int lenghtOfFile = c.getContentLength();
                Log.d("Downloading Updates", "Lenght of file: " + lenghtOfFile);

                String PATH = Environment.getExternalStorageDirectory() + "/araboard/";
                file = new File(PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outputFile = new File(file, app_name);
                FileOutputStream fos = new FileOutputStream(outputFile);



                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                long total = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    total += len1;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    fos.write(buffer, 0, len1); // Write In FileOutputStream.
                }
                fos.flush();
                fos.close();
                is.close();
                //Unzip
                Utils.unzip(outputFile,file);
            } catch (Exception e) {
                Log.d("Yosu",e.getMessage());
            }
            return null;

        }
        protected void onProgressUpdate(String... progress) {
            Log.d("Downloading Updates",progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            finish();
        }
    }




}
