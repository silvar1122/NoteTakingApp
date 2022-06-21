package com.example.notetakingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notetakingapp.Adapters.NotesListAdapter;
import com.example.notetakingapp.Database.RoomDB;
import com.example.notetakingapp.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes> notes_list=new ArrayList<>();
    RoomDB database;
    FloatingActionButton btn_add;
    SearchView search_view_home;
    Notes selected_note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recycler_home);
        btn_add=(FloatingActionButton) findViewById(R.id.fab_add);
        search_view_home=findViewById(R.id.search_view_home);

        database=RoomDB.getInstance(getApplicationContext());

        notes_list=database.mainDAO().getAll();


        updateRecycler(notes_list);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NotesTakerActivity.class);
                startActivityForResult(intent,101);

            }
        });
        search_view_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

    }

    private void filter(String newText) {
        List<Notes>filtered_list=new ArrayList<>();
        for(Notes single_note: notes_list)
        {
            if(single_note.getTitle().toLowerCase().contains(newText.toLowerCase())
            || single_note.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filtered_list.add(single_note);

            }

        }
        notesListAdapter.FilteredList(filtered_list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101){
            if(resultCode== Activity.RESULT_OK ){

                Notes new_notes= (Notes) data.getSerializableExtra("note");
                database.mainDAO().insert(new_notes);
                notes_list.clear();
                notes_list.addAll(database.mainDAO().getAll());
//                Toast.makeText(getApplicationContext(), Integer.toString(notes_list.size()), Toast.LENGTH_SHORT).show();
//
//                Toast.makeText(this, "ID="+Integer.toString(notes_list.get(notes_list.size()-1).getID()), Toast.LENGTH_SHORT).show();
//
                notesListAdapter.notifyDataSetChanged();

            }
            else{
                Toast.makeText(this, "wrong", Toast.LENGTH_SHORT).show();
            }
        }

        else if(requestCode==102){
            if(resultCode==Activity.RESULT_OK){
                Notes new_notes= (Notes) data.getSerializableExtra("note");
                database.mainDAO().update(new_notes.getID(),new_notes.getNotes(),new_notes.getTitle());
                notes_list.clear();
                notes_list.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();

            }

        }
    }


    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter=new NotesListAdapter(getApplicationContext(),notes,notesClickListener);
        recyclerView.setAdapter(notesListAdapter);


    }

    private final NotesClickListener notesClickListener=new NotesClickListener(){
        @Override
        public void onClick(Notes notes) {

            Intent intent=new Intent(MainActivity.this,NotesTakerActivity.class);
            intent.putExtra("old_note",notes);
            startActivityForResult(intent,102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {

            selected_note=new Notes();
            selected_note=notes;
            showPopup(cardView);

        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu=new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.pin:
                if(selected_note.isPinned()){
                    database.mainDAO().Pin(selected_note.getID(),false);
                    Toast.makeText(this, "Unpinned", Toast.LENGTH_SHORT).show();

                }
                else{
                    database.mainDAO().Pin(selected_note.getID(),true);
                    Toast.makeText(this, "Pinned", Toast.LENGTH_SHORT).show();
                }
                notes_list.clear();
                notes_list.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
                return true;

            case R.id.delete:
                database.mainDAO().delete(selected_note);
                notes_list.remove(selected_note);
                notesListAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }

    }
}