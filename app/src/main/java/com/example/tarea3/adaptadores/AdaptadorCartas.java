package com.example.tarea3.adaptadores;

import static com.example.tarea3.clases.Url.BASE_URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.tarea3.R;
import com.example.tarea3.clases.CartaImagen;

import java.util.ArrayList;

import javax.xml.transform.ErrorListener;

public class AdaptadorCartas extends RecyclerView.Adapter<AdaptadorCartas.ViewHolderCartas>
        implements View.OnClickListener{

    ArrayList<CartaImagen> listImagenes;
    RequestQueue request;
    Context context;
    private View.OnClickListener listener;

    public AdaptadorCartas(ArrayList<CartaImagen> listImagenes, Context context) {
        this.listImagenes = listImagenes;
        this.context = context;
        request = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public ViewHolderCartas onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carta_imagen, parent, false);
        view.setOnClickListener(this);
        return new ViewHolderCartas(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderCartas holder, int position) {
        String url = BASE_URL + "/" + listImagenes.get(position).getRuta();
        //Log.d("ruta", url);

        ImageRequest imgReq = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.foto.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No se respondio bien", Toast.LENGTH_LONG).show();
            }
        });
        request.add(imgReq);
    }


    @Override
    public int getItemCount() {
        return listImagenes.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class ViewHolderCartas extends RecyclerView.ViewHolder {
        ImageView foto;

        public ViewHolderCartas(View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.cartaImg);
        }

    }
}
