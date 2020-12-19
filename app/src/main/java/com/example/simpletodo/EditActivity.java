package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText etText;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etText = findViewById(R.id.etText);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit item");

        // put contents from main recycler view item to edit text field.
        // to get text of item, call getStringExtra
        etText.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // when user is done editing, click save button
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // create intent which will contain results
                Intent intent = new Intent();
                // pass data (from the editing field)
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, etText.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                // set the result of intent
                setResult(RESULT_OK, intent);
                // finish activity and close screen to go back
                finish();

            }
        });
    }
}