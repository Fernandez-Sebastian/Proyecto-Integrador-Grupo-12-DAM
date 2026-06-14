package com.example.myapplication.activities

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        db.execSQL(
            """
            CREATE TABLE Socios(
                id_socio INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                apellido TEXT,
                dni TEXT UNIQUE,
                fecha_nacimiento TEXT,
                apto_medico INTEGER,
                habilitado INTEGER
            )
            """
        )
        db.execSQL(
            """
            CREATE TABLE Cuota(
                id_cuota INTEGER PRIMARY KEY AUTOINCREMENT,
                numero_cuota INTEGER NOT NULL,
                fecha_pago TEXT,
                fecha_inicio TEXT NOT NULL,
                fecha_fin TEXT NOT NULL,
                monto REAL NOT NULL DEFAULT 45000,
                metodo_pago TEXT,
                vigente TEXT NOT NULL DEFAULT 'N',
                cantidad_cuota_financiada TEXT NOT NULL DEFAULT '1',
                estado TEXT NOT NULL DEFAULT 'Impaga',
                id_socio INTEGER,
                FOREIGN KEY(id_socio) REFERENCES Socios(id_socio)
            )
            """
        )
        db.execSQL(
            """
        CREATE TABLE Carnet(
            id_carnet INTEGER PRIMARY KEY AUTOINCREMENT,
            fecha_emision TEXT NOT NULL,
            fecha_vencimiento TEXT NOT NULL,
            numero TEXT UNIQUE,
            id_socio INTEGER,
            FOREIGN KEY(id_socio) REFERENCES Socios(id_socio)
            )
            """
        )

        db.execSQL("INSERT INTO Actividad (nombre, dia, horario, precio_actividad) VALUES ('Fútbol', 'Jueves', '10:30', 5000.00)")
        db.execSQL("INSERT INTO Actividad (nombre, dia, horario, precio_actividad) VALUES ('Natación', 'Lunes', '09:00', 8000.00)")
        db.execSQL("INSERT INTO Actividad (nombre, dia, horario, precio_actividad) VALUES ('Yoga', 'Miércoles', '11:30', 4000.00)")
        db.execSQL("INSERT INTO Actividad (nombre, dia, horario, precio_actividad) VALUES ('Pilates', 'Viernes', '08:00', 6000.00)")
        db.execSQL("INSERT INTO Actividad (nombre, dia, horario, precio_actividad) VALUES ('Musculación', 'Jueves', '10:30', 3500.00)")
        db.execSQL("INSERT INTO Actividad (nombre, dia, horario, precio_actividad) VALUES ('Padle', 'Sábado', '09:00', 8000.00)")

        // Datos para socios
        db.execSQL("INSERT INTO Socios(nombre, apellido, dni, fecha_nacimiento, apto_medico, habilitado) VALUES ('Glaucia', 'Ferreira', '95789456', '19/06/1996', 1, 1)")
        db.execSQL("INSERT INTO Socios(nombre, apellido, dni, fecha_nacimiento, apto_medico, habilitado) VALUES ('Andrea', 'Maslucan', '95639789', '10/05/1995', 1, 1)")
        db.execSQL("INSERT INTO Socios(nombre, apellido, dni, fecha_nacimiento, apto_medico, habilitado) VALUES ('Sebastián', 'Fernández', '95471268', '15/03/1994', 1, 1)")
        db.execSQL("INSERT INTO Socios(nombre, apellido, dni, fecha_nacimiento, apto_medico, habilitado) VALUES ('Ignacio', 'Grosman', '95843258', '20/08/1993', 1, 1)")

        // Datos para cuotas
        db.execSQL("INSERT INTO Cuota(numero_cuota, fecha_pago, fecha_inicio, fecha_fin, monto, metodo_pago, vigente, cantidad_cuota_financiada, estado, id_socio) VALUES (1, NULL, '2026-05-01', '2026-05-31', 45000.00, NULL, 'S', '1', 'Impaga', 1)")
        db.execSQL("INSERT INTO Cuota(numero_cuota, fecha_pago, fecha_inicio, fecha_fin, monto, metodo_pago, vigente, cantidad_cuota_financiada, estado, id_socio) VALUES (1, '2026-05-05', '2026-05-01', '2026-05-31', 45000.00, 'Transferencia', 'S', '1', 'Paga', 2)")
        db.execSQL("INSERT INTO Cuota(numero_cuota, fecha_pago, fecha_inicio, fecha_fin, monto, metodo_pago, vigente, cantidad_cuota_financiada, estado, id_socio) VALUES (1, NULL, '2026-05-01', '2026-05-31', 45000.00, NULL, 'S', '1', 'Impaga', 3)")
        db.execSQL("INSERT INTO Cuota(numero_cuota, fecha_pago, fecha_inicio, fecha_fin, monto, metodo_pago, vigente, cantidad_cuota_financiada, estado, id_socio) VALUES (1, '2026-05-06', '2026-05-01', '2026-05-31', 45000.00, 'Tarjeta', 'S', '1', 'Paga', 4)")

        // Datos para carnet
        db.execSQL("INSERT INTO Carnet(fecha_emision, fecha_vencimiento, numero, id_socio) VALUES ('2026-05-01', '2027-05-01', 95789456, 1)")
        db.execSQL("INSERT INTO Carnet(fecha_emision, fecha_vencimiento, numero, id_socio) VALUES ('2026-05-01', '2027-05-01', 95639789, 2)")
        db.execSQL("INSERT INTO Carnet(fecha_emision, fecha_vencimiento, numero, id_socio) VALUES ('2026-05-01', '2027-05-01', 95471268, 3)")
        db.execSQL("INSERT INTO Carnet(fecha_emision, fecha_vencimiento, numero, id_socio) VALUES ('2026-05-01', '2027-05-01', 95843258, 4)")
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

    fun insertarSocio(nombre: String, apellido: String, dni: String, fechaNacimiento: String, aptoMedico: Int, habilitado: Int): Long {
        val db = writableDatabase
        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("apellido", apellido)
        valores.put("dni", dni)
        valores.put("fecha_nacimiento", fechaNacimiento)
        valores.put("apto_medico", aptoMedico)
        valores.put("habilitado", habilitado)

        return db.insert("Socios", null, valores)
    }

    fun existeSocioPorDni(dni: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id_socio FROM Socios WHERE dni = ?",
            arrayOf(dni)
        )

        val existe = cursor.count > 0
        cursor.close()
        return existe
    }

    fun buscarIdSocioPorDni(dni: String): Int? {

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT id_socio FROM Socios WHERE dni = ? "
                .trimIndent(),
            arrayOf(dni)
        )

        val idSocio = if (cursor.moveToFirst()) {
            cursor.getInt(0)
        } else {
            null
        }

        cursor.close()

        return idSocio
    }

    fun crearCarnet(idSocio: Int, numero: String): Long {
        val db = writableDatabase

        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val fechaEmisionCalendar = Calendar.getInstance()
        val fechaEmision = formatoFecha.format(fechaEmisionCalendar.time)

        val fechaVencimientoCalendar = Calendar.getInstance()
        fechaVencimientoCalendar.add(Calendar.YEAR, 1)
        val fechaVencimiento = formatoFecha.format(fechaVencimientoCalendar.time)

        val valores = ContentValues()
        valores.put("fecha_emision", fechaEmision)
        valores.put("fecha_vencimiento", fechaVencimiento)
        valores.put("numero", numero)
        valores.put("id_socio", idSocio)

        return db.insert("Carnet", null, valores)
    }

    fun existeCarnetSocio(dni: String): Boolean {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
                    SELECT 1
                    FROM Carnet c
                    INNER JOIN Socios s
                        ON c.id_socio = s.id_socio
                    WHERE s.dni = ?
                    LIMIT 1
                """.trimIndent(),
            arrayOf(dni)
        )

        val existe = cursor.moveToFirst()

        cursor.close()

        return existe
    }

    fun obtenerDatosCarnetPorDni(dni: String): Map<String, String>? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
                SELECT s.nombre, s.apellido, c.fecha_vencimiento
                FROM Socios s
                INNER JOIN Carnet c ON s.id_socio = c.id_socio
                WHERE s.dni = ?
            """.trimIndent(),
            arrayOf(dni)
        )

        return if (cursor.moveToFirst()) {
            val nombre = cursor.getString(0)
            val apellido = cursor.getString(1)
            val vencimiento = cursor.getString(2)
            cursor.close()
            mapOf(
                "nombreCompleto" to "${nombre} ${apellido}",
                "vencimiento" to vencimiento
            )
        } else {
            cursor.close()
            null
        }
    }

    fun insertarPrimeraCuotaSocio(idSocio: Int): Long {
        val db = writableDatabase

        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val fechaInicioCalendar = Calendar.getInstance()
        val fechaFinCalendar = Calendar.getInstance()
        fechaFinCalendar.add(Calendar.MONTH, 1)

        val valores = ContentValues()
        valores.put("numero_cuota", 1)
        valores.putNull("fecha_pago")
        valores.put("fecha_inicio", formatoFecha.format(fechaInicioCalendar.time))
        valores.put("fecha_fin", formatoFecha.format(fechaFinCalendar.time))
        valores.put("monto", 45000.0)
        valores.putNull("metodo_pago")
        valores.put("vigente", "S")
        valores.put("cantidad_cuota_financiada", "1")
        valores.put("estado", "Impaga")
        valores.put("id_socio", idSocio)

        return db.insert("Cuota", null, valores)
    }

    fun existenVencimientosHoy(): Boolean {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT 1
            FROM Cuota c
            INNER JOIN Socios s
                ON c.id_socio = s.id_socio
            WHERE c.estado = 'Impaga'
                AND date(c.fecha_fin) <= date('now', 'localtime')
                AND NOT EXISTS (
                    SELECT 1
                    FROM Cuota cf
                    WHERE cf.id_socio = s.id_socio
                        AND date(cf.fecha_fin) > date('now', 'localtime')
                        AND cf.estado = 'Paga'
                )
            LIMIT 1
            """.trimIndent(),
            null
        )

        val existen = cursor.moveToFirst()

        cursor.close()

        return existen
    }

    fun obtenerVencimientosSocios(): List<Triple<String, String, String>> {
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
                SELECT 
                    s.nombre || ' ' || s.apellido AS nombre_completo,
                    s.dni,
                    CASE 
                        WHEN c.estado = 'Impaga'
                            AND date(c.fecha_fin) <= date('now', 'localtime')
                            AND NOT EXISTS (
                                SELECT 1
                                FROM Cuota cf
                                WHERE cf.id_socio = s.id_socio
                                    AND date(cf.fecha_fin) > date('now', 'localtime')
                                    AND cf.estado = 'Paga'
                            )
                        THEN 'Vencido'
                        ELSE 'Al día'
                    END AS estado_visual
                FROM Socios s
                INNER JOIN Cuota c 
                    ON c.id_socio = s.id_socio
                WHERE c.vigente = 'S'
                ORDER BY s.apellido, s.nombre
            """.trimIndent(),
            null
        )

        val lista = mutableListOf<Triple<String, String, String>>()

        while (cursor.moveToNext()) {
            val nombreCompleto = cursor.getString(0)
            val dni = cursor.getString(1)
            val estado = cursor.getString(2)

            lista.add(Triple(nombreCompleto, dni, estado))
        }

        cursor.close()
        return lista
    }
}