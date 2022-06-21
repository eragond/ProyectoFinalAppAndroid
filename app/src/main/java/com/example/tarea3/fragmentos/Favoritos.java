package com.example.tarea3.fragmentos;
import static com.example.tarea3.clases.Url.BASE_URL;
import static com.example.tarea3.clases.Url.NOCHES;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tarea3.R;
import com.example.tarea3.actividades.VistaImagenGrande;
import com.example.tarea3.adaptadores.AdaptadorCartas;
import com.example.tarea3.adaptadores.AdaptadorFavoritos;
import com.example.tarea3.clases.CartaImagen;
import com.example.tarea3.clases.ConexionSQLiteHelper;
import com.example.tarea3.clases.InstSQL;
import com.google.android.material.snackbar.Snackbar;

import java.net.ConnectException;
import java.util.ArrayList;

public class Favoritos extends Fragment {

    protected ArrayList<CartaImagen> listImagen;
    protected RecyclerView recycler;
    protected AdaptadorFavoritos adaptador;

    //private ProgressDialog dialog;
    private RequestQueue request;
    private JsonObjectRequest jsonObjReq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recycler = (RecyclerView) view.findViewById(R.id.recyclerFavoritosVista);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));


        listImagen = new ArrayList<CartaImagen>();
        request = Volley.newRequestQueue(getContext());

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getContext());
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor curs = db.rawQuery(InstSQL.TRAE_RUTAS, null);
        if (curs.getCount() == 0) {
            Snackbar.make(view, "No hay favoritos!!", Snackbar.LENGTH_LONG).show();
        } else {
            try {
                curs.moveToFirst();
                while(curs.moveToNext()) {
                    listImagen.add(new CartaImagen("Nombre", curs.getString(0)));
                }
            } catch (Error e){
                e.printStackTrace();
                Toast.makeText(getContext(), "La base de datos no encontró favoritos", Toast.LENGTH_LONG).show();
            } finally {
                curs.close();
            }
        }

        adaptador = new AdaptadorFavoritos(listImagen, getContext());

        adaptador.setOnItemClickListener(new AdaptadorFavoritos.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), VistaImagenGrande.class);
                intent.putExtra("fotoRuta", listImagen.get(position).getRuta());
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