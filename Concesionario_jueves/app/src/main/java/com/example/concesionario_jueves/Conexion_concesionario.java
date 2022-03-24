package com.example.concesionario_jueves;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Conexion_concesionario extends SQLiteOpenHelper {

    public Conexion_concesionario(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table TblCliente(Identificacion text primary key,nombre text not null," +
                "usuario text not null,clave text not null, activo text not null default 'si')");
        sqLiteDatabase.execSQL("CREATE TABLE TblVehiculo(placa text primary key," +
                "marca text not null, modelo text not null, color text not null,costo text not null ,activo text not null default 0)");
        sqLiteDatabase.execSQL("CREATE TABLE TblFactura(codFactura text primary key," +
                "fecha text not null, identificacion text not null, placa text not null,activo text not null default 0, " +
                "constraint pkFctura foreign key (identificacion) references TblCliente(Identificacion)," +
                " foreign key (placa) references TblVehiculo(placa) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE TblCliente");{
            onCreate(sqLiteDatabase);
        }
        sqLiteDatabase.execSQL("DROP TABLE TblVehiculo");{
            onCreate(sqLiteDatabase);
        }

        sqLiteDatabase.execSQL("DROP TABLE TblFactura");{
            onCreate(sqLiteDatabase);
        }
    }
}
