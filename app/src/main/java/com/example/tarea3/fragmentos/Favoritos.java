package com.example.tarea3.fragmentos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarea3.R;
import com.example.tarea3.actividades.VistaImagenGrande;
import com.example.tarea3.adaptadores.AdaptadorFavoritos;
import com.example.tarea3.clases.CartaImagen;
import com.example.tarea3.clases.ConexionSQLiteHelper;
import com.example.tarea3.clases.InstSQL;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Favoritos extends Fragment {

    protected ArrayList<CartaImagen> listImagen;
    protected RecyclerView recycler;
    protected AdaptadorFavoritos adaptador;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favoritos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recycler = (RecyclerView) view.findViewById(R.id.recyclerFavoritosVista);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        listImagen = new ArrayList<CartaImagen>();

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getContext());
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor curs = db.rawQuery(InstSQL.TRAE_RUTAS, null);
        if (curs.getCount() == 0) {
            Snackbar.make(view, "No hay favoritos!!", Snackbar.LENGTH_LONG).show();
        } else {
            try {
                while (curs.moveToNext()) {
                    listImagen.add(new CartaImagen("Nombre", curs.getString(0)));
                }
            } catch (Error e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "La base de datos no encontró favoritos", Toast.LENGTH_LONG).show();
            }
        }
        curs.close();
        db.close();

        adaptador = new AdaptadorFavoritos(listImagen, getContext());

        adaptador.setOnItemClickListener(new AdaptadorFavoritos.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), VistaImagenGrande.class);
                intent.putExtra("fotoRuta", listImagen.get(position).getRuta());
                intent.putExtra("inFavorites", true);
                getContext().startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getContext());
                SQLiteDatabase db = conn.getWritableDatabase();
                String ruta = listImagen.get(position).getRuta();
                String[] sa = {ruta};
                db.delete(InstSQL.N_TABLE, InstSQL.RUTA + "=?", sa);
                db.close();

                listImagen.remove(position);
                adaptador.notifyItemRemoved(position);
                Toast.makeText(getContext(), "Eliminado de favoritos", Toast.LENGTH_LONG).show();
            }
        });

        recycler.setAdapter(adaptador);

    }


}