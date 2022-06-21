package com.example.tarea3.fragmentos;

import static com.example.tarea3.clases.Url.BASE_URL;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tarea3.R;
import com.example.tarea3.actividades.VistaImagenGrande;
import com.example.tarea3.adaptadores.AdaptadorCartas;
import com.example.tarea3.clases.CartaImagen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GridMaker extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    public String suburl = "";

    protected ArrayList<CartaImagen> listImagen;
    protected RecyclerView recycler;
    protected AdaptadorCartas adaptador;

    private ProgressDialog dialog;
    private RequestQueue request;
    private JsonObjectRequest jsonObjReq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_grid, container, false);
        this.suburl = "";
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recycler = (RecyclerView) view.findViewById(R.id.recyclerGrid);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 3));

        listImagen = new ArrayList<CartaImagen>();
        request = Volley.newRequestQueue(getContext());

        cargarWebService();

        adaptador = new AdaptadorCartas(listImagen, getContext());
        recycler.setAdapter(adaptador);

    }

    private void cargarWebService() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Consultando");
        dialog.show();

        String url = BASE_URL + "/" + suburl;
        jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjReq);
    }

    @Override
    public void onResponse(JSONObject response) {
        CartaImagen imga = null;
        try {
            JSONArray jarra = response.getJSONArray("imagenes");
            for (int i = 0; i < jarra.length(); i++) {
                JSONObject jojo = jarra.getJSONObject(i);
                imga = new CartaImagen(jojo.getString("nombre"), jojo.getString("ruta"));
                listImagen.add(imga);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "El json response no fue parceable", Toast.LENGTH_LONG).show();
        }
        dialog.hide();
        Toast.makeText(getContext(), "Respuesta correcta", Toast.LENGTH_LONG).show();

        adaptador.notifyDataSetChanged();

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), VistaImagenGrande.class);
                intent.putExtra("fotoRuta", listImagen.get(recycler.getChildAdapterPosition(view)).getRuta());
                getContext().startActivity(intent);
            }
        });

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se respondio bien", Toast.LENGTH_LONG).show();
        dialog.hide();
        Log.e("ERROR_RESPONSE: ", error.toString());
    }
}