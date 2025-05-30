package com.example.meal.db.IngrediantDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.meal.model.pojo.ingrediant.Ingrediant;
@Database(entities = {Ingrediant.class}, version = 1, exportSchema = false)
public abstract class IngrediantDataBase extends RoomDatabase{
     private static IngrediantDataBase instance;

     public abstract IngrediantDAO getIngrediantDao();

        public static synchronized IngrediantDataBase getInstance(Context context) {
            if (instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(), IngrediantDataBase.class, "IngredientDB")
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return instance;
        }

}
