package com.example.proyectofinal.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.proyectofinal.models.Note
import com.example.proyectofinal.utils.DateConverter

@Database(entities = [Note::class], version = 130)
@TypeConverters(DateConverter::class)
abstract class NoteAppDataBase : RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteAppDataBase? = null

        fun getDatabase(context: Context): NoteAppDataBase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteAppDataBase::class.java,
                        "note_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
