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

public class FacturaActivity extends AppCompatActivity {
    EditText codigo, fecha, identificacion, placa;
    Button guardar, consultar, anular, cancelar, regresar;
    TextView txtActivo;
    long resp, sw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);
        codigo = findViewById(R.id.etcodigo);
        fecha = findViewById(R.id.etfecha);
        identificacion = findViewById(R.id.etidentificacion);
        placa = findViewById(R.id.etplaca);
        guardar = findViewById(R.id.btguardar);
        consultar = findViewById(R.id.btconsultar);
        cancelar = findViewById(R.id.btcancelar);
        anular = findViewById(R.id.btanular);
        regresar = findViewById(R.id.btregresar);
        txtActivo = findViewById(R.id.txtActivo);
        txtActivo.setVisibility(View.INVISIBLE);
    }

    public void guardar(View view) {
        String scodigo, sfecha, sidentificacion, splaca;
        scodigo = codigo.getText().toString();
        sfecha = fecha.getText().toString();
        sidentificacion = identificacion.getText().toString();
        splaca = placa.getText().toString();
        gotoguardar(scodigo, sfecha, sidentificacion, splaca);
        txtActivo.setVisibility(View.INVISIBLE);
        limpiar();
    }

    public void consultar(View view){
        String scodigo = codigo.getText().toString();
        gotoconsultar(scodigo);
    }

    public void gotoconsultar(String scodigo) {
         if (scodigo.isEmpty()){
             Toast.makeText(this, "Codigo requerido", Toast.LENGTH_SHORT).show();
             codigo.requestFocus();
         }else {
             Conexion_concesionario admin = new Conexion_concesionario(this, "concesionario5.bd", null, 1);
             SQLiteDatabase db=admin.getReadableDatabase();
             Cursor fila=db.rawQuery("select * from TblFactura where codFactura='" + scodigo + "'",null);
             if (fila.moveToNext()){
                 sw=1;
                 fecha.setText(fila.getString(1));
                 identificacion.setText(fila.getString(2));
                 placa.setText(fila.getString(3));
                 String activo=fila.getString(4);
                 int a = Integer.parseInt(activo);

                 if (a==0){
                     txtActivo.setVisibility(View.VISIBLE);
                     txtActivo.setText("La factura Esta activa");

                 }else{
                     txtActivo.setVisibility(View.VISIBLE);
                     txtActivo.setText("La factura Esta anulada");

                 }
             } else {
                 Toast.makeText(this, "Factura no hallada", Toast.LENGTH_SHORT).show();
             }
             db.close();
         }
    }
    public void AnularFactura(View view){
        String scodigo = codigo.getText().toString();
        String splaca= placa.getText().toString();
        gotoconsultar(scodigo);
        if (sw==1){
            Conexion_concesionario admin = new Conexion_concesionario(this,"concesionario5.bd",null,1);
            SQLiteDatabase db = admin.getReadableDatabase();
            ContentValues dato = new ContentValues();
            ContentValues activar = new ContentValues();
            dato.put("placa",splaca);
            dato.put("activo",1);
            activar.put("activo",0);

            resp=db.update("TblFactura",dato,"codFactura='"+scodigo+"'",null);
            if (resp>0){
                resp=db.update("TblVehiculo",activar,"placa='"+splaca+"'",null);
                Toast.makeText(this, "La factura fue anulada", Toast.LENGTH_SHORT).show();
                limpiar();
            }else{
                Toast.makeText(this, "Error al anular la factura ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void gotoguardar(String scodigo, String sfecha, String sidentificacion, String splaca) {
        if (scodigo.isEmpty() || sfecha.isEmpty() || sidentificacion.isEmpty() || splaca.isEmpty()) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            codigo.requestFocus();
        } else {
            Conexion_concesionario admin = new Conexion_concesionario(this, "concesionario5.bd", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();
            try {
                SQLiteDatabase dbsearch = admin.getReadableDatabase();
                String sql1 = "select Identificacion from TblCliente where Identificacion = '" + sidentificacion+"'";
                Cursor cursorcliente = dbsearch.rawQuery(sql1, null);
                if (cursorcliente.moveToFirst()){
                    String sql2 = "select placa from TblVehiculo where placa = '" + splaca + "'and activo = 0  ";
                    Cursor cursorplaca = dbsearch.rawQuery(sql2, null);
                    if (cursorplaca.moveToFirst()){
                        SQLiteDatabase db2 = admin.getWritableDatabase();
                        ContentValues guarda = new ContentValues();
                        ContentValues anular = new ContentValues();
                        guarda.put("codFactura",scodigo);
                        guarda.put("fecha",sfecha);
                        guarda.put("Identificacion",sidentificacion);
                        guarda.put("placa",splaca);
                        guarda.put("activo",0);
                        // Luis te wa matar pvto -
                        anular.put("activo",1);

                        if (sw==0){
                            resp= db2.insert("TblFactura",null,guarda);
                            resp=db2.update("TblVehiculo",anular,"placa='"+splaca+"'",null);
                        }
                       else {
                           sw=0;
                           resp=db2.update("TblFactura",guarda,"codFactura='" + scodigo +"'", null);

                        }
                       if (resp > 0){
                           Toast.makeText(getApplicationContext(), "La factura se ha agregado correctamente ...", Toast.LENGTH_SHORT).show();
                       }
                       else {
                           Toast.makeText(getApplicationContext(), "Error guardando factura", Toast.LENGTH_SHORT).show();
                       }
                        db2.close();

                    }else {
                        Toast.makeText(getApplicationContext(), "Error, la placa no existe o no esta disponible", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Error, la identificacion no existe", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }
    public void limpiar(){
        sw=0;
        codigo.setText("");
        fecha.setText("");
        identificacion.setText("");
        placa.setText("");
        codigo.requestFocus();
        txtActivo.setText("");
        txtActivo.setVisibility(View.INVISIBLE);
    }
    public void regresar(View view){
        Intent menu = new Intent(this,MenuActivity.class);
        startActivity(menu);
    }
    public void cancelar(View view){limpiar();}


}
