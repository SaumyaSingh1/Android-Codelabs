package com.example.sanjeev.roomwordsample;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * This class defines a word entity.
 * words are the data for our app.
 */

@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")  // By default, column name is same as member variable
    private String mWord;

    public Word(@NonNull String word){
        this.mWord = word;
    }

    // Each field in database must be either public or have a getter method
    public String getWord(){
        return this.mWord;
    }
}
