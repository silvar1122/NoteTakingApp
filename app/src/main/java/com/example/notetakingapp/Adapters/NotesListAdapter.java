package com.example.notetakingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorLong;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notetakingapp.Models.Notes;
import com.example.notetakingapp.NotesClickListener;
import com.example.notetakingapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.viewHolder> {

    Context context;
    List<Notes> list;
    NotesClickListener listener;

    public NotesListAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.tv_title.setText(list.get(position).getTitle());
        holder.tv_title.setSelected(true);
        holder.tv_notes.setText(list.get(position).getNotes());
        holder.tv_date.setText(list.get(position).getDate());
        holder.tv_date.setSelected(true);

        if(list.get(position).isPinned()){
            holder.img_pin.setImageResource(R.drawable.ic_pin);
        }
        else{holder.img_pin.setImageResource(0);
        }
        int color_code=getRandomColor();
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code,null));


        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                listener.onLongClick(list.get(holder.getAdapterPosition()),holder.notes_container);
                return true;
            }
        });
    }

    private int getRandomColor(){
        List<Integer>colorCode=new ArrayList<>();

        colorCode.add(R.color.color_1);
        colorCode.add(R.color.color_2);
        colorCode.add(R.color.color_3);
        colorCode.add(R.color.color_4);
        colorCode.add(R.color.color_5);
        colorCode.add(R.color.color_6);

        Random random=new Random();
        int random_color=random.nextInt(colorCode.size());
        return colorCode.get(random_color);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void FilteredList(List<Notes> filteredList){
        list=filteredList;
        notifyDataSetChanged();

    }

    class viewHolder extends RecyclerView.ViewHolder {
        CardView notes_container;
        TextView tv_title, tv_notes,tv_date;
        ImageView img_pin;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            notes_container=itemView.findViewById(R.id.notes_container);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_notes=itemView.findViewById(R.id.tv_notes);
            tv_date=itemView.findViewById(R.id.tv_date);
            img_pin=itemView.findViewById(R.id.img_pin);
        }
    }
}

