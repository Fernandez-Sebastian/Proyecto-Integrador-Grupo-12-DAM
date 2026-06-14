package com.example.myapplication.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.utils.FooterManager

class ComprobantePagoCuotaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprobante_pago_cuota)

        // BOTÓN REGRESAR (del header.xml)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvHeaderTitle = findViewById<TextView>(R.id.tvHeaderTitle)
        
        tvHeaderTitle?.text = "Comprobante Pago de Cuota"
        btnBack?.setOnClickListener {
            finish()
        }

        val tvNombre = findViewById<TextView>(R.id.tvNombre)
        val tvDni = findViewById<TextView>(R.id.tvDni)
        val tvMonto = findViewById<TextView>(R.id.tvMonto)
        val tvMetodo = findViewById<TextView>(R.id.tvMetodo)
        val btnDescargar = findViewById<Button>(R.id.btnDescargar)
        val btnVolverMenu = findViewById<Button>(R.id.btnVolverMenu)

        // DATOS RECIBIDOS
        val nombre = intent.getStringExtra("nombre")
        val dni = intent.getStringExtra("dni")
        val monto = intent.getStringExtra("monto")
        val metodo = intent.getStringExtra("metodo")

        // SETEO DATOS
        tvNombre.text = "Socio: $nombre"
        tvDni.text = "DNI: $dni"
        tvMonto.text = "Monto: $monto"
        tvMetodo.text = "Método: $metodo"

        btnDescargar.setOnClickListener {
            Toast.makeText(this,"Descarga exitosa",Toast.LENGTH_SHORT).show()
        }

        btnVolverMenu.setOnClickListener {
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