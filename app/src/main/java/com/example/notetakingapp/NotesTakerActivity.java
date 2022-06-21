package com.example.notetakingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notetakingapp.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTakerActivity extends AppCompatActivity {

    EditText et_notes,et_title;
    ImageView img_save;
    Notes notes;
    boolean isOldNote=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);

        et_title=(EditText) findViewById(R.id.et_title);
        et_notes=(EditText) findViewById(R.id.et_notes);
        img_save=(ImageView) findViewById(R.id.img_save);


        notes=new Notes();
        try{
            notes= (Notes) getIntent().getSerializableExtra("old_note");
            et_title.setText(notes.getTitle());
            et_notes.setText(notes.getNotes());
            isOldNote=true;

        }catch (Exception e){
            e.printStackTrace();
        }

        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=et_title.getText().toString();
                String the_notes=et_notes.getText().toString();

                if(the_notes.isEmpty()){
                    Toast.makeText(NotesTakerActivity.this, "Please add some notes ", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat formatter=new SimpleDateFormat("EEE,d MMM yyy HH::mm a");
                Date date=new Date();

                if(!isOldNote){
                    notes=new Notes();
                }

                notes.setTitle(title);
                notes.setNotes(the_notes);
                notes.setDate(formatter.format(date));


                Intent intent=new Intent();
                intent.putExtra("note",notes);
                setResult(Activity.RESULT_OK,intent);
                finish();


            }
        });

    }
}