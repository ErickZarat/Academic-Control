package org.erickzarat.academiccontrol.helper;

import android.app.Activity;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by erick on 4/06/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreate = "CREATE TABLE Utilities (id INTEGER, name TEXT, value TEXT)";

    //constructor 1
    public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //contructor 2
    public DatabaseHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Utilities");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }

    public void insert(int id, String name, String value){
        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        //DatabaseHelper dbh = new DatabaseHelper(activity.getApplicationContext(), "Utilities", null, 1);

        SQLiteDatabase db = getWritableDatabase();

        //Si hemos abierto correctamente la base de datos
        if(db != null)
        {
            //Insertamos los datos en la tabla Usuarios
            db.execSQL("INSERT INTO Utilities (id, name, value) " +
                    "VALUES (" + id + ", '" + name +"', '" + value + "')");

            //Cerramos la base de datos
            db.close();
        }
    }

    public void delete(int id, String name, String value ){
        SQLiteDatabase db = getWritableDatabase();

        if(db != null){
            //delete
            db.execSQL("DELETE FROM Utilities WHERE name='"+ name +"'");

            db.close();
        }
    }
}
