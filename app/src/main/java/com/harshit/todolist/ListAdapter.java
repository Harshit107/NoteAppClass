package com.harshit.todolist;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder>{

    private static final String TAG = "AdapterClass";
    Context context;
    ArrayList<MyList> arrayList;
    Activity activity;

    public ListAdapter(Context context, ArrayList<MyList> arrayList, MainActivity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.item.setText(arrayList.get(position).getText());
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, holder.getAdapterPosition()+"",Toast.LENGTH_SHORT).show();
                removeFromMain(holder.getAdapterPosition());
                arrayList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String text = arrayList.get(holder.getAdapterPosition()).getText();
                removeFromMain(holder.getAdapterPosition());
                arrayList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                ((MainActivity)activity).editView(text);

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView item;
        ImageView close;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            close = itemView.findViewById(R.id.close);

        }
    }

    public void renewItem( ArrayList<MyList> lists) {
        this.arrayList = lists;
        notifyDataSetChanged();
    }

    public void removeFromMain(int pos) {
        ((MainActivity)activity).removeItem(pos);

    }


}
