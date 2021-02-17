package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    //Need information to fill the adapter which will be passed in from MainActivity.java
    //Main piece of information needed is about the model - list of strings
    //Adapter is constructed in MainActivity.java

    //interface to be implemented in MainActivity.java so we can talk to the adapter behind the RecyclerView
    public interface OnLongClickListener
    {
        //need to take in position so we can notify the adapter what item we long-clicked on
        void onItemLongClicked(int position);
    }


    List<String> items;
    OnLongClickListener longClickListener;

    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener)
    {
        this.items = items;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    //responsible for creating each view
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Use layout inflator to inflate a view

            //ViewGroup is a parameter
            //pass in the XML file of the view being created to inflate()
                //Here, we use a built-in Android resource file called simple_list_item_1
            //pass in the root, i.e. parent
            //pass in false, else the RecyclerView will attach this view instead of attaching it to the root
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        //wrap it inside a ViewHolder and return it
        return new ViewHolder(todoView);
    }

    @Override
    //responsible for taking data at a particular position and putting it into a ViewHolder
    //OR responsible for binding data to a particular holder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Grab the item at the position
        String item = items.get(position); //posiiton is a parameter
        //Bind the item into the specified ViewHolder
        holder.bind(item); //bind() is a method we define in the ViewHolder class
    }

    @Override
    //number of items available in the data
    //OR, tells the RecyclerView how many items are in the list
    public int getItemCount() {
        return items.size();
    }

    //Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{

        //Need a reference to view that we can access in the method bind()
        //This TextView is a reference to the TextView inside simple_list_item_1

        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //view inside the view passed to the ViewHolder
            //need to say android.R. because simple_list_item_1 is a built-in Android resource
            tvItem = itemView.findViewById(android.R.id.text1);

        }


        //Update view inside the ViewHolder with this data - item
        public void bind(String item) {
            //text on the TextView is set to the contents of the item passed in
            tvItem.setText(item);
            tvItem.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    //When Android notifies us that an item was long-pressed, we are notify the
                    //listener of the position that was long-pressed
                    //getAdapterPosition() gets the position of the ViewHolder
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    //callback is consuming the long click
                    return true;
                }
            });
        }
    }
}
