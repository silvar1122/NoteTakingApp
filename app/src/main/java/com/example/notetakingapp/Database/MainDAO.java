package com.example.notetakingapp.Database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.notetakingapp.Models.Notes;

import java.util.List;

@Dao
public interface MainDAO
{

    @Insert(onConflict = REPLACE)
    void insert(Notes notes);

    @Query("SELECT * FROM notes ORDER BY ID DESC")
    List<Notes> getAll();

    @Query("UPDATE notes SET title=:title,notes=:notes WHERE ID=:ID")
    void update(int ID,String notes,String title);

    @Delete
    void delete(Notes notes);

    @Query("UPDATE notes SET pinned=:pin WHERE ID=:id")
    void Pin(int id,boolean pin);
}
