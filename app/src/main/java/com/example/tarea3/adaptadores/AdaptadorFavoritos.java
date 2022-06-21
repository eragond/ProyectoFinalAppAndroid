package com.example.tarea3.adaptadores;

import static com.example.tarea3.clases.Url.BASE_URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class AdaptadorFavoritos extends RecyclerView.Adapter<AdaptadorFavoritos.ViewHolderFavoritos>{
    ArrayList<CartaImagen> listImagenes;
    RequestQueue request;
    Context context;
    OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { mListener = listener;}

    public AdaptadorFavoritos(ArrayList<CartaImagen> listImagenes, Context context) {
        this.listImagenes = listImagenes;
        this.context = context;
        request = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public ViewHolderFavoritos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carta_favoritos, parent, false);
        return new ViewHolderFavoritos(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorFavoritos.ViewHolderFavoritos holder, int position) {
        String url = BASE_URL + "/" + listImagenes.get(position).getRuta();

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

    public class ViewHolderFavoritos extends RecyclerView.ViewHolder {
        ImageView foto;
        ImageButton delBtn;

        public ViewHolderFavoritos(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            foto = itemView.findViewById(R.id.cartaFavImg);
            delBtn = itemView.findViewById(R.id.delBtn);

            itemView.setOnClickListener((view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            }));

            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }

    }
}
