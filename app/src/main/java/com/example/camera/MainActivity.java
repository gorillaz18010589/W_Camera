package com.example.camera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private File sdroot;
    private ImageView img;
    private  String imageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //次要詢問多個權限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            //一開始沒有權限的話
            requestRuntimePermission(); //去要多個權限方法
            Log.v("hank", "沒權限去要");
        } else {
            //有權限的話
            init();
            Log.v("hank", "有權限");

        }

        init();

    }

    private void init() {
        sdroot = Environment.getExternalStorageDirectory();
        img = findViewById(R.id.img);
        imageName = UUID.randomUUID().toString();
    }

    //要多個權限方法
    private void requestRuntimePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.CAMERA, //相機
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, //外部儲存寫入
                        Manifest.permission.READ_EXTERNAL_STORAGE //讀取外部
                },
                12);
    }


    public void test4(View view) {
        Uri photoURI = FileProvider.getUriForFile(this, getPackageName() + ".provider",
                new File(sdroot + "/DCIM/Camera", imageName + "jpg"));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, 4);
        Log.v("hank", "btnCamera");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4 && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(sdroot.getAbsolutePath() + "/DCIM/Camera" + "/" + imageName + "jpg");
            img.setImageBitmap(bitmap);
            Log.v("hank", "onActivityResult => OK");
        }
    }
}
