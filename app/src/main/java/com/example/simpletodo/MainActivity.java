package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button buttonAdd;
    EditText editTextItem;
    RecyclerView recyclerView;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = findViewById(R.id.btnAdd);
        editTextItem = findViewById(R.id.etItem);
        recyclerView = findViewById(R.id.rvItems);


        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                // delete item from model
                items.remove(position);
                // notify which position was deleted.
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext() ,"Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "SIngle click at position " + position);
                // create new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                //pass data to be edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                //display activity
                startActivityForResult(i, EDIT_TEXT_CODE);



            }
        };
        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = editTextItem.getText().toString();
                // add item to model
                items.add(todoItem);
                // notify adapter about inserted item
                itemsAdapter.notifyItemInserted(items.size() - 1);
                editTextItem.setText("");
                Toast.makeText(getApplicationContext() ,"Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    // handle result from edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            // get updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // get original position of edited item from position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            // update model at the right position with new item text
            items.set(position, itemText);
            // notify adapter
            itemsAdapter.notifyItemChanged(position);
            // persist changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // loads item by reading everylien of data.txt
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // saves items by writing to file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}