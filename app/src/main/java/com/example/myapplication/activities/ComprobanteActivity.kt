package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.utils.FooterManager


class ComprobanteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprobante)
        val tvHeaderTitle = findViewById<TextView>(R.id.tvHeaderTitle)

        tvHeaderTitle?.text = "Comprobante pago de actividad"

        //Volver atrás
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        //Card del comprobante
        val tv = findViewById<TextView>(R.id.tvComprobante)

        val nombre = intent.getStringExtra("nombre")
        val dni = intent.getStringExtra("dni")
        val actividad = intent.getStringExtra("actividad")
        val medioPago = intent.getStringExtra("medioPago")
        val precio = intent.getStringExtra("precio")
        val fecha = intent.getStringExtra("fecha")
        val hora = intent.getStringExtra("hora")

        tv.text = """
        NoSocio: $nombre
        DNI: $dni
        Monto: $precio
        Método: $medioPago
        Fecha: $fecha
        Hora: $hora
        Actividad: $actividad
        """.trimIndent()

        //Descargar comprobante
        val btnDescargar = findViewById<Button>(R.id.btnDescargar)

        btnDescargar.setOnClickListener {
            Toast.makeText(this, "Comprobante descargado con éxito",
                Toast.LENGTH_SHORT).show()
        }

        //Volver al menú principal
        val btnVolver = findViewById<Button>(R.id.btnVolverInicio)

        btnVolver.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // --- LÓGICA DEL FOOTER ---
        FooterManager.setupFooter(
            activity = this,
            showWhiteBar = true,
            showHome = true,
            showSettings = true,
            showLogout = true
        )
    }
}