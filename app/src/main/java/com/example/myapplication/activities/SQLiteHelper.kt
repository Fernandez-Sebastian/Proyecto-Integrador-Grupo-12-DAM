package com.example.myapplication.activities

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context): SQLiteOpenHelper(context, "clubdeportivo.db", null, 1){

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            //Raw String
            """
            CREATE TABLE NoSocios(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                apellido TEXT,
                dni TEXT,
                fecha_nacimiento TEXT,
                apto_medico INTEGER
            )    
            """
        )
        db.execSQL(
            //Raw String
            """
            CREATE TABLE Actividad(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                dia TEXT,
                horario TEXT,
                precio_actividad REAL
            )    
            """
        )
        db.execSQL("INSERT INTO Actividad (nombre, dia, horario, precio_actividad) VALUES ('Fútbol', 'Jueves', '10:30', 5000.00)")
        db.execSQL("INSERT INTO Actividad (nombre, dia, horario, precio_actividad) VALUES ('Natación', 'Lunes', '09:00', 8000.00)")
        db.execSQL("INSERT INTO Actividad (nombre, dia, horario, precio_actividad) VALUES ('Yoga', 'Miércoles', '11:30', 4000.00)")
        db.execSQL("INSERT INTO Actividad (nombre, dia, horario, precio_actividad) VALUES ('Pilates', 'Viernes', '08:00', 6000.00)")
        db.execSQL("INSERT INTO Actividad (nombre, dia, horario, precio_actividad) VALUES ('Musculación', 'Jueves', '10:30', 3500.00)")
        db.execSQL("INSERT INTO Actividad (nombre, dia, horario, precio_actividad) VALUES ('Padle', 'Sábado', '09:00', 8000.00)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertarNoSocio(nombre: String, apellido: String, dni: String, fechaNacimiento: String, aptoMedico: Int){
        val db = writableDatabase
        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("apellido", apellido)
        valores.put("dni", dni)
        valores.put("fecha_nacimiento", fechaNacimiento)
        valores.put("apto_medico", aptoMedico)
        db.insert("NoSocios", null, valores)
    }

    fun existeNoSocioPorDni(dni: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id FROM NoSocios WHERE dni = ?", arrayOf(dni))
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }

    fun buscarNoSocioPorDni(dni: String): Pair<String, String>? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT nombre, apellido FROM NoSocios WHERE dni = ?", arrayOf(dni))
        return if (cursor.moveToFirst()) {
            val nombre = cursor.getString(0)
            val apellido = cursor.getString(1)
            cursor.close()
            Pair(nombre, apellido)
        } else {
            cursor.close()
            null
        }
    }

    fun obtenerActividades(): List<Triple<String, String, Double>> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT nombre, dia, precio_actividad FROM Actividad", null)
        val lista = mutableListOf<Triple<String, String, Double>>()
        while (cursor.moveToNext()) {
            val nombre = cursor.getString(0)
            val dia = cursor.getString(1)
            val precio = cursor.getDouble(2)
            lista.add(Triple(nombre, dia, precio))
        }
        cursor.close()
        return lista
    }
}