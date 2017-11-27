package com.sitecdesarro.gymapp.ui;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.sitecdesarro.gymapp.R;

import org.w3c.dom.Text;

public class AboutActivity extends AppCompatActivity {

    private TextView acerca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        acerca = (TextView) findViewById(R.id.txtAcerca);
        acerca.setText(Html.fromHtml(getString(R.string.acerca)));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


    }
}
