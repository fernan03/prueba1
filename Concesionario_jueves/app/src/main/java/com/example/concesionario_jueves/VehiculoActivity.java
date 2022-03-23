package com.example.concesionario_jueves;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VehiculoActivity extends AppCompatActivity {

    EditText jetplaca, jetmarca, jetmodelo, jetcosto, jetcolor,jetdisponible;
    Button jbtguardar, jbtconsultar, jbtanular, jbtingresar, jbtcancelar, jbtregresar;
    long resp, sw;
    String placa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo);

        getSupportActionBar().hide();
        jetplaca = findViewById(R.id.etplaca);
        jetmarca = findViewById(R.id.etmarca);
        jetmodelo = findViewById(R.id.etmodelo);
        jetcosto = findViewById(R.id.etcosto);
        jetcolor = findViewById(R.id.etcolor);
        jetdisponible = findViewById(R.id.etdisponible);

        jbtguardar = findViewById(R.id.btguardar);
        jbtanular = findViewById(R.id.btanular);
        jbtcancelar = findViewById(R.id.btcancelar);
        jbtconsultar = findViewById(R.id.btconsultar);
        jbtingresar = findViewById(R.id.btingresar);
        jbtregresar = findViewById(R.id.btregresar);
    }

    public void Guardar(View view) {
        String marca, modelo, color, costo,disponible;
        placa = jetplaca.getText().toString();
        marca = jetmarca.getText().toString();
        modelo = jetmodelo.getText().toString();
        color = jetcolor.getText().toString();
        costo = jetcosto.getText().toString();
        disponible = jetdisponible.getText().toString();

        if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() || color.isEmpty() || costo.isEmpty() || disponible.isEmpty()) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        } else {
            Conexion_concesionario admin = new Conexion_concesionario(this, "concesionario5.bd", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("placa", placa);
            registro.put("marca", marca);
            registro.put("modelo", modelo);
            registro.put("color", color);
            registro.put("costo", costo);
            registro.put("activo",disponible);
            ConsultarVehiculo();
            if (sw == 1) {
                sw = 0;
                resp = db.update("TblVehiculo", registro, "placa='" + placa + "'", null);
            } else {
                resp = db.insert("TblVehiculo", null, registro);
            }

            if (resp > 0) {
                Limpiar_campos();
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error en guardar el registro", Toast.LENGTH_SHORT).show();
            }
              db.close();
        }
    }

    public void Consulta_vehiculo(View view) {
        ConsultarVehiculo();
    }

    public void ConsultarVehiculo() {

        placa = jetplaca.getText().toString();
        if (placa.isEmpty()) {
            Toast.makeText(this, "La placa es requerida para buscar", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        } else {
            Conexion_concesionario admin = new Conexion_concesionario(this, "concesionario5.bd", null, 1);
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor fila = db.rawQuery("select * from TblVehiculo where placa='" + placa + "'", null);
            if (fila.moveToNext()) {
                sw = 1;
                jetmarca.setText(fila.getString(1));
                jetmodelo.setText(fila.getString(2));
                jetcolor.setText(fila.getString(3));
                jetcosto.setText(fila.getString(4));
                jetdisponible.setText(fila.getString(5));
            } else {

            }
             db.close();
        }
    }

    public void AnularVehiculo(View view) {
        ConsultarVehiculo();
        if (sw == 1) {
            Conexion_concesionario admin = new Conexion_concesionario(this, "concesionario5.bd", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("placa", placa);
            registro.put("activo", 1);
            resp = db.update("TblVehiculo", registro, "placa='" + placa + "'", null);
            if (resp > 0) {
                Toast.makeText(this, "Registro Anulado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            } else {
                Toast.makeText(this, "Error al anular el registro", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "La placa no esta registrada", Toast.LENGTH_SHORT).show();
        }
    }

    public void Limpiar_campos() {
        sw = 0;
        jetplaca.setText("");
        jetcolor.setText("");
        jetcosto.setText("");
        jetmarca.setText("");
        jetmodelo.setText("");
        jetdisponible.setText("");
        jetplaca.requestFocus();
    }

    public void Regresar(View view) {
        Intent main = new Intent(this, MenuActivity.class);
        startActivity(main);
    }
}