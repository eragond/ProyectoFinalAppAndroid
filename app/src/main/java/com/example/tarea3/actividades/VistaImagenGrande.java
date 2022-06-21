package com.example.tarea3.actividades;

import static com.example.tarea3.clases.Url.BASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.FileOutputStream;

public class VistaImagenGrande extends AppCompatActivity {

    ImageView imgGde;
    Button share, fav, save;
    RequestQueue request;
    String ruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_imagen_grande);

        imgGde = findViewById(R.id.imagenGrande);
        share = findViewById(R.id.btnShare);
        fav = findViewById(R.id.btnFav);
        save = findViewById(R.id.btnSave);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Bundle extras = getIntent().getExtras();
        if (extras.getBoolean("inFavorites"))
            fav.setVisibility(View.GONE);

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
                ConexionSQLiteHelper conn = new ConexionSQLiteHelper(view.getContext());
                SQLiteDatabase db = conn.getWritableDatabase();
                String ints = InstSQL.insertFavoritoRuta(ruta);
                db.execSQL(ints);
                db.close();
                Toast.makeText(view.getContext(), "Agregado a favoritos ", Toast.LENGTH_LONG).show();
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SAVE", "El peluca sabeeeee");
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgGde.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                saveToGallery(bitmap);
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
            e.printStackTrace();
        }
        return uri;
    }

    private void saveToGallery(Bitmap btm) {
        if (android.os.Build.VERSION.SDK_INT < 29)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);

        MediaStore.Images.Media.insertImage(getContentResolver(), btm, "" , "");
        Toast.makeText(this, "Imagen guardada", Toast.LENGTH_LONG).show();

        /*
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
        File salida = new File(storageDir, timeStamp+"buen_dia.png");
        try {
            FileOutputStream out = new FileOutputStream(salida);
            btm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "La imagen no pudo ser guardada" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        */

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}