package com.example.tarea3.fragmentos.comunes;

import static com.example.tarea3.clases.Url.VIERNES;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tarea3.fragmentos.GridMaker;

public class SabadoFragment extends GridMaker {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        this.suburl = VIERNES;
        return view;
    }
}