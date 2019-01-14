package com.example.ufimets.danceperson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {
String[] arr;
TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        arr = getIntent().getStringArrayExtra("CODE");
result = findViewById(R.id.textView2);
result.setText(arr[0] + " Вы чертовски зафлекссили!\n Набрав " + arr[1] + " очков");

    }
}
