package com.example.proyectofinal.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey
    @SerializedName("id")
    var id: String = "",
    @ColumnInfo(name = "titulo")
    @SerializedName("titulo")
    var title: String,
    @ColumnInfo(name = "body")
    @SerializedName("body")
    var body: String,
    @ColumnInfo(name = "fecha")
    @SerializedName("fecha")
    var date: Date,
    @ColumnInfo(name = "latitud")
    @SerializedName("latitud")
    var latitude: Double = 0.0,
    @ColumnInfo(name = "longitud")
    @SerializedName("longitud")
    var longitude: Double = 0.0,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    var userId: String
)
