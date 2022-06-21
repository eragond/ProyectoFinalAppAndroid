package com.example.tarea3.clases;

import android.security.AppUriAuthenticationPolicy;

public final class InstSQL {
    private InstSQL() {}

    public static final String NOMBRE_DB = "hardbass";
    public static final String N_TABLE = "imgfav";
    public static final String RUTA = "ruta";

    public static final String CREA_TABLA_IMGFAV = "CREATE TABLE " + N_TABLE +
                                " (" + RUTA + " TEXT, UNIQUE( " + RUTA + " ))";
    public static final String BORRA_TABLA_IMGFAV = "DROP TABLE IF EXISTS " + N_TABLE;
    public static final String INSERT_FAVORITOS = "INSERT OR IGNORE INTO " + N_TABLE +
                                " ( " + RUTA + " )  VALUES ( \'";

    public static final String REMOVE_FAV = "DELETE FROM " + N_TABLE + " WHERE " + RUTA + " = \'";

    public static final String TRAE_RUTAS = "SELECT " + RUTA + " FROM " + N_TABLE;


    public static String insertFavoritoRuta(String ruta) {
        return INSERT_FAVORITOS + ruta + "\' )";
    }

    public static String removeFavoritoRuta(String ruta) {
        return REMOVE_FAV + ruta + "\'";
    }

}
