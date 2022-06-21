package com.example.tarea3.actividades;

import static com.example.tarea3.clases.InstSQL.NOMBRE_DB;
import static com.example.tarea3.clases.Url.BASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ClipData;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.tarea3.R;
import com.example.tarea3.clases.ConexionSQLiteHelper;
import com.example.tarea3.clases.InstSQL;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

public class VistaImagenGrande extends AppCompatActivity {

    ImageView imgGde;
    Button share, fav;
    RequestQueue request;
    String ruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_imagen_grande);

        imgGde = findViewById(R.id.imagenGrande);
        share = findViewById(R.id.btnShare);
        fav = findViewById(R.id.btnFav);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Bundle extras = getIntent().getExtras();

        ruta = extras.getString("fotoRuta");
        String url = BASE_URL + "/" + ruta;

        request = Volley.newRequestQueue(this);

        ImageRequest imgReq = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imgGde.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "No se respondio bien", Toast.LENGTH_LONG).show();
            }
        });
        request.add(imgReq);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Agregado a favoritos ", Toast.LENGTH_LONG).show();
                ConexionSQLiteHelper conn = new ConexionSQLiteHelper(view.getContext());
                SQLiteDatabase db = conn.getWritableDatabase();
                String ints = InstSQL.insertFavoritoRuta(ruta);
                db.execSQL(ints);
                db.close();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgGde.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                Uri uri = getImageToShare(bitmap);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setClipData(ClipData.newRawUri("", uri));
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType("image/png");

                startActivity(Intent.createChooser(intent, "Share Via"));
            }
        });


    }

    // Obteniendo el uri para compartir
    private Uri getImageToShare(Bitmap bitmap) {
        File imagefolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(this, "com.example.tarea3.fileprovider", file);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}