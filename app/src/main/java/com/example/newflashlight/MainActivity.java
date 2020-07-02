package com.example.newflashlight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button enableButton;
    ImageButton flashOnButton, flashOffButton;
    private boolean flashLightStatus;
    private static final int CAMERA_REQUEST = 50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableButton = findViewById(R.id.buttonEnable);

        flashOnButton = findViewById(R.id.flash_on);
        flashOffButton = findViewById(R.id.flash_off);

        final boolean hasCameraFlash = getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        boolean isEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;

        enableButton.setEnabled(!isEnabled);
        flashOnButton.setEnabled(isEnabled);

        enableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.CAMERA
                },CAMERA_REQUEST);
            }
        });

        flashOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasCameraFlash)
                {
                    if(!flashLightStatus)
                    {
                        flashLightOn();
                    }
                }
                else Toast.makeText(MainActivity.this, "No Flash Available", Toast.LENGTH_SHORT).show();
            }
        });

        flashOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(hasCameraFlash)
                {
                    if(flashLightStatus)
                    {
                        flashLightOff();
                    }
                }
                else Toast.makeText(MainActivity.this, "No Flash Available", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void flashLightOn()
    {



        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            flashOnButton.setVisibility(View.GONE);
            flashOffButton.setVisibility(View.VISIBLE);
            flashLightStatus = true;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void flashLightOff()
    {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            flashOnButton.setVisibility(View.VISIBLE);
            flashOffButton.setVisibility(View.GONE);
            flashLightStatus = false;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case CAMERA_REQUEST:
                if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                        enableButton.setEnabled(false);
                        enableButton.setText("Camera Enabled");
                        flashOnButton.setEnabled(true);
                }
                else
                    {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                break;
                
        }
    }
}