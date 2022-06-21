package com.example.notetakingapp;

import androidx.cardview.widget.CardView;

import com.example.notetakingapp.Models.Notes;

public interface NotesClickListener {
    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);
}
