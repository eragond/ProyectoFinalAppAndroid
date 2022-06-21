package com.example.tarea3.actividades;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.tarea3.R;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tarea3.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private static MediaPlayer gallo;
    private static MediaPlayer musica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musica.isPlaying()) {
                    musica.pause();
                    binding.appBarMain.fab.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                } else {
                    musica.start();
                    binding.appBarMain.fab.setImageResource(R.drawable.ic_baseline_pause_24);
                }

            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.top, R.id.favoritos, R.id.lunesNavFrag, R.id.martesNavFrag,
                R.id.miercolesNavFrag, R.id.juevesNavFrag, R.id.viernesNavFrag, R.id.sabadoNavFrag,
                R.id.domingoNavFrag, R.id.diosNavFrag, R.id.humorNavFrag, R.id.nochesNavFrag,
                R.id.piolinNavFrag, R.id.floresNavFrag)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        // Inicio del media
        gallo = MediaPlayer.create(this, R.raw.rooster);
        musica = MediaPlayer.create(this, R.raw.loving_you);

        musica.start();
        musica.setLooping(true);
        gallo.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem croco = menu.findItem(R.id.botonGallo);
        croco.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                gallo.start();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}