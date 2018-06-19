package ru.miklelolyandex.officeexercises;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Query("select * from Exercise")
    List<Exercise> getAll();

    @Query("select * from Exercise where exercise_set_id = :setID and id = :id")
    Exercise getByID(long setID, long id);

    @Insert
    void insert (Exercise exerciseEntity);

    @Update
    void update (Exercise exerciseEntity);

    @Delete
    void delete (Exercise exerciseEntity);

    @Insert
    void insertAll(Exercise... dataEntities);
}
