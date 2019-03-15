package com.styleru.curry.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.styleru.curry.data.models.recipe.Recipe;

import java.util.List;

@Dao
public abstract class RecipeDAO {

    @Query("select * from recipe")
    public abstract List<Recipe> getRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Recipe recipe);

    @Delete
    public abstract void delete(Recipe recipe);
}
