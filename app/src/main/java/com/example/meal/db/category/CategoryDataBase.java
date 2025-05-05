package com.example.meal.db.category;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.meal.model.pojo.category.Category;

@Database(entities = {Category.class}, version = 1, exportSchema = false)
public abstract class CategoryDataBase extends RoomDatabase {
    private static CategoryDataBase instance;

    public abstract CategoeyDAO getCategoryDao();

    public static synchronized CategoryDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CategoryDataBase.class, "CategoryDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
