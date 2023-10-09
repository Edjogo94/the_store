package com.thestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActividadPrincipal extends AppCompatActivity {
    private Button agregarButton, btnLista, btnTopVentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);
        agregarButton = (Button) findViewById(R.id.btnAgregar);
        agregarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistroProductos.class);
                startActivity(intent);
            }
        });


        btnLista = (Button) findViewById(R.id.btnLista);
        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductList.class);
                startActivity(intent);
            }
        });

        btnTopVentas = (Button) findViewById(R.id.btnTopVentas);
        btnTopVentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TopVentas.class);
                startActivity(intent);
            }
        });
    }
}