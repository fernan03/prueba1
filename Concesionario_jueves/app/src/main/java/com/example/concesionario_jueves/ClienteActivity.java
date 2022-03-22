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
import android.widget.TextView;
import android.widget.Toast;

public class ClienteActivity extends AppCompatActivity {

    EditText jetidentificacion,jetnombre,jetusuario,jetclave1,jetclave2;
    TextView jtvactivo;
    Button jbtguardar,jbtconsultar,jbtanular,jbtcancelar,jbtregresar;
    long resp;
    int sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        getSupportActionBar().hide();
        jetidentificacion=findViewById(R.id.etidentificacion);
        jetnombre=findViewById(R.id.etnombre);
        jetusuario=findViewById(R.id.etusuario);
        jetclave1=findViewById(R.id.etclave1);
        jetclave2=findViewById(R.id.etclave2);
        jtvactivo=findViewById(R.id.tvactivo);
        jbtguardar=findViewById(R.id.btguardar);
        jbtconsultar=findViewById(R.id.btconsultar);
        jbtanular=findViewById(R.id.btanular);
        jbtcancelar=findViewById(R.id.btcancelar);
        jbtregresar=findViewById(R.id.btregresar);
        sw=0;
    }

    public void limpiar_campos(){
        sw=0;
        jetidentificacion.setText("");
        jetnombre.setText("");
        jetusuario.setText("");
        jetclave1.setText("");
        jetclave2.setText("");
        jetidentificacion.requestFocus();
    }

    public void Guardar(View view){
        String identificacion, nombre, usuario,clave1,clave2;
        identificacion=jetidentificacion.getText().toString();
        nombre=jetnombre.getText().toString();
        usuario=jetusuario.getText().toString();
        clave1=jetclave1.getText().toString();
        clave2=jetclave2.getText().toString();
        if (identificacion.isEmpty() || nombre.isEmpty() || usuario.isEmpty()
        || clave1.isEmpty() || clave2.isEmpty()) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
        else{
            if (!clave1.equals(clave2)){
                Toast.makeText(this, "Clave y confirmacion de clave son diferentes", Toast.LENGTH_SHORT).show();
                jetclave1.requestFocus();
            }
            else{
                Conexion_concesionario admin=new Conexion_concesionario(this,"concesionario5.bd",null,1);
                SQLiteDatabase db=admin.getWritableDatabase();
                ContentValues dato=new ContentValues();
                dato.put("Identificacion",identificacion);
                dato.put("nombre",nombre);
                dato.put("usuario",usuario);
                dato.put("clave",clave1);
                if (sw == 0)
                    resp=db.insert("TblCliente",null,dato);
                else{
                    sw=0;
                    resp=db.update("TblCliente",dato,"identificacion='" + identificacion + "'",null);
                }
                if (resp > 0){
                    Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                    limpiar_campos();
                }
                else{
                    Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        }
    }

    public void Consultar(View view){
        Consultar_Cliente();
    }

    public void Consultar_Cliente(){
        String identificacion;
        identificacion=jetidentificacion.getText().toString();
        if (identificacion.isEmpty()){
            Toast.makeText(this, "Identificacion requerida", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
        else {
            Conexion_concesionario admin = new Conexion_concesionario(this, "concesionario5.bd", null, 1);
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor fila=db.rawQuery("select * from TblCliente where identificacion='" + identificacion + "'",null);
            if (fila.moveToNext()){
                sw=1;
                jetnombre.setText(fila.getString(1));
                jetusuario.setText(fila.getString(2));
                jetclave1.setText(fila.getString(3));
                Toast.makeText(this, "Registrado encontrado", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Registro no existe", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }

    public void Anular(View view){
        Consultar_Cliente();
        if (sw == 1){
            String identificacion=jetidentificacion.getText().toString();
            Conexion_concesionario admin=new Conexion_concesionario(this,"concesionario5.bd",null,1);
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues dato=new ContentValues();
            dato.put("idcliente",identificacion);
            dato.put("activo","no");
            resp=db.update("TblCliente",dato,"idcliente='" + identificacion + "'",null);
            if (resp > 0){
                Toast.makeText(this, "Registro anulado", Toast.LENGTH_SHORT).show();
                limpiar_campos();
            }
            else {
                Toast.makeText(this, "Error anulando registro", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }else{
            Toast.makeText(this, "La identificacion no fue encontrada", Toast.LENGTH_SHORT).show();
        }

    }

    public void Cancelar(View view){
        limpiar_campos();
    }

    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }
}