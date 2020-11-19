package com.game.onecricket.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.onecricket.R;

public class RoiInfo extends Activity implements View.OnClickListener {
    public ImageView gotit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roi_info);
         gotit=findViewById(R.id.roibuttton);
        gotit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.roibuttton:
                System.out.println("not closing");
                finish();
        }
    }
}