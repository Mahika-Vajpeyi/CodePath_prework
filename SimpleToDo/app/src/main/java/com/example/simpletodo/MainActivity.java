package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items; //model

    //reference to each view in this file so we can add appropriate logic
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //define member variables - have each view as a reference
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        /*
        //instantiate our model
        items = new ArrayList<>();
         */
        //loading previously saved list
        loadItems();

        /*
        //mock data
        items.add("Buy milk");
        items.add("Go to the gym");
        items.add("Have fun!");
         */

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //delete the item at position from the model - list of strings
                items.remove(position);
                //Notify the adapter of the position at the item deleted
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        //sets the adapter on the recycler view
        rvItems.setAdapter(itemsAdapter);

        //LinearLayoutManager: most basic layout manager, displays items in te UI in a linear fashion
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            //onClick means that someone clicked on our button
            public void onClick(View v) {
                //gets text from the edit text and returns an editable, so we change it to a string
                String todoItem = etItem.getText().toString();
                //Add item to the model
                items.add(todoItem);
                //Notify the adapter that an item was added
                itemsAdapter.notifyItemInserted(items.size() - 1);

                //Clear the item from the edit text after submitting the item
                etItem.setText("");

                //Toast: small pop-up that tells the user the item was added and then disappears
                //Toast.LENGTH_SHORT: duration
                //call show method to display hte toast
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }
        //Methods to implement persistence
        //These methods are private becasue they will be called inside this file


        //returns the file in which to store the list od to-do items
        //getFilesDir(): gets the directory of the app
        private File getDataFile()
        {
            return new File(getFilesDir(), "data.txt");
        }

        //This function will load items by reading every line of the data file
        private void loadItems()
        {
            //read all lines in the data file to populate the array list which is our model
            try {
                items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
            } catch (IOException e) {
                //uses logging so we, the developer, can know what's happening
                //Use class name as tag name by convention
                Log.e("MainActivity", "Error reading items", e);
                //empty list so we have something to build our RecyclerView off of in case we
                //encounter an exception
                items = new ArrayList<>();
            }
        }


        //This function saves items by writing them into the data file
        private void saveItems()
        {
            try {
                FileUtils.writeLines(getDataFile(), items);
            } catch (IOException e) {
                //pass in e so we can print what the error was
                Log.e("MainActivity", "Error writing items", e);
            }

        }

}
