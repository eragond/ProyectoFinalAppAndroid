package com.example.tarea3.clases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {
    public ConexionSQLiteHelper(@Nullable Context context) {
        super(context, InstSQL.NOMBRE_DB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(InstSQL.CREA_TABLA_IMGFAV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int verAntigua, int verNueva) {
        sqLiteDatabase.execSQL(InstSQL.BORRA_TABLA_IMGFAV);
        onCreate(sqLiteDatabase);
    }
}
