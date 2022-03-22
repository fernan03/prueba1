package com.example.concesionario_jueves;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText jetusuario,jetclave;
    Button jbtingresar,jbtregistrarse,jbtcancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        jetusuario=findViewById(R.id.etusuario);
        jetclave=findViewById(R.id.etclave);
        jbtingresar=findViewById(R.id.btingresar);
        jbtregistrarse=findViewById(R.id.btregistrarse);
        jbtcancelar=findViewById(R.id.btcancelar);
    }

    public void Ingresar(View view){
        String usuario,clave;
        usuario=jetusuario.getText().toString();
        clave=jetclave.getText().toString();
        if (usuario.isEmpty() || clave.isEmpty()){
            Toast.makeText(this, "Usuario y clave requeridos", Toast.LENGTH_SHORT).show();
            jetusuario.requestFocus();
        }
        else{
            Conexion_concesionario admin = new Conexion_concesionario(this, "concesionario5.bd", null, 1);
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor fila=db.rawQuery("select usuario from TblCliente where usuario='" + usuario + "' and clave='" + clave + "'",null);
            if (fila.moveToNext()){
                Intent intvehiculo=new Intent(this,MenuActivity.class);
                startActivity(intvehiculo);
            }
            else{
                Toast.makeText(this, "Usuario o clave invalidos", Toast.LENGTH_SHORT).show();
                jetusuario.requestFocus();
            }
            db.close();
        }
    }

    public void Registrarse(View view){
        Intent intregistrarse=new Intent(this,ClienteActivity.class);
        startActivity(intregistrarse);
    }

    public void Cancelar(View view){
        jetusuario.setText("");
        jetclave.setText("");
        jetusuario.requestFocus();
    }
}