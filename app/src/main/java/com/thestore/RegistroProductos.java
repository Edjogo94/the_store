package com.thestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import db.Sqlite_OpenHelper;

public class RegistroProductos extends AppCompatActivity {

    private EditText nombreProductoEditText;
    private EditText descripcionEditText;
    private Spinner spinnerCategoria;
    private EditText marcaEditText;
    private EditText inventarioEditText;
    private EditText precioEditText;
    private EditText imagenUrlEditText;
    private Button registrarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_productos);

        nombreProductoEditText = findViewById(R.id.inputNombreProducto);
        descripcionEditText = findViewById(R.id.inputDescripcionProducto);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        marcaEditText = findViewById(R.id.inputMarca);
        inventarioEditText = findViewById(R.id.inputInventario);
        precioEditText = findViewById(R.id.inputPrecio);
        imagenUrlEditText = findViewById(R.id.inputImagenUrl);
        registrarButton = findViewById(R.id.btnRegistrarProducto);

        String[] categorias = getResources().getStringArray(R.array.categorias_productos);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        registrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreProducto = nombreProductoEditText.getText().toString();
                String descripcion = descripcionEditText.getText().toString();
                String categoria = spinnerCategoria.getSelectedItem().toString();
                String marca = marcaEditText.getText().toString();
                String inventarioStr = inventarioEditText.getText().toString();
                String precioStr = precioEditText.getText().toString();
                String imagenUrl = imagenUrlEditText.getText().toString();

                if (nombreProducto.length() < 3 || descripcion.length() < 3) {
                    Toast.makeText(getApplicationContext(), "Nombre y descripción deben tener al menos 3 caracteres.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (categoria.isEmpty() || marca.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Categoría y marca son campos obligatorios.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int inventario;
                double precio;

                try {
                    inventario = Integer.parseInt(inventarioStr);
                    precio = Double.parseDouble(precioStr);

                    if (inventario <= 0 || precio <= 0) {
                        Toast.makeText(getApplicationContext(), "Inventario y precio deben ser números mayores a 0.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Inventario y precio deben ser números válidos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidImageUrl(imagenUrl)) {
                    Toast.makeText(getApplicationContext(), "URL de imagen no válida.", Toast.LENGTH_SHORT).show();
                    return;
                }

                long resultado = insertarProducto(nombreProducto, descripcion, categoria, marca, inventario, precio, imagenUrl);

                if (resultado != -1) {
                    Toast.makeText(getApplicationContext(), "Producto registrado con éxito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ActividadPrincipal.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error al registrar el producto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidImageUrl(String url) {
        return url.startsWith("http");
    }

    private long insertarProducto(String nombreProducto, String descripcion, String categoria, String marca, int inventario, double precio, String imagenUrl) {
        Sqlite_OpenHelper dbHelper = new Sqlite_OpenHelper(getApplicationContext(), getResources().getString(R.string.DATABASE_NAME), null, 1);
        return dbHelper.insertProduct(nombreProducto, descripcion, categoria, marca, inventario, precio, imagenUrl);
    }
}