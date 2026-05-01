package com.example.myapplication.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R


class ComprobanteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprobante)

        val tv = findViewById<TextView>(R.id.tvComprobante)

        val nombre = intent.getStringExtra("nombre")
        val dni = intent.getStringExtra("dni")
        val actividad = intent.getStringExtra("actividad")
        val medioPago = intent.getStringExtra("medioPago")
        val precio = intent.getStringExtra("precio")
        val fecha = intent.getStringExtra("fecha")
        val hora = intent.getStringExtra("hora")

        tv.text = """
        Socio: $nombre
        DNI: $dni
        Monto: $precio
        Método: $medioPago
        Fecha: $fecha
        Hora: $hora
        Actividad: $actividad
        """.trimIndent()
    }
}