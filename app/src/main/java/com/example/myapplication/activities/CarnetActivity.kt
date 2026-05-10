package com.example.myapplication.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import com.example.myapplication.utils.FooterManager

class CarnetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet)

        // Configurar Header
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvHeaderTitle = findViewById<TextView>(R.id.tvHeaderTitle)
        
        tvHeaderTitle?.text = "Carnet de Socio"
        btnBack?.setOnClickListener {
            finish()
        }

        // Referencias de la UI para búsqueda
        val etBuscarDni = findViewById<EditText>(R.id.etBuscarDni)
        val ivLupa = findViewById<ImageView>(R.id.ivLupa)
        val tvMensajeBusqueda = findViewById<TextView>(R.id.tvMensajeBusqueda)
        val clContenidoSocio = findViewById<ConstraintLayout>(R.id.clContenidoSocio)

        // Lógica de búsqueda
        ivLupa.setOnClickListener {
            val dni = etBuscarDni.text.toString().trim()
            
            if (dni == "1234567") {
                Toast.makeText(this, getString(R.string.socio_encontrado), Toast.LENGTH_SHORT).show()
                
                // Mostrar carnet
                tvMensajeBusqueda.visibility = View.GONE
                clContenidoSocio.visibility = View.VISIBLE
                
            } else if (dni.isNotEmpty()) {
                // Toast para socio inexistente
                Toast.makeText(this, getString(R.string.socio_inexistente), Toast.LENGTH_SHORT).show()
                
                // Estado inicial
                tvMensajeBusqueda.visibility = View.VISIBLE
                clContenidoSocio.visibility = View.GONE
            }
        }

        // Botón Imprimir Carnet
        findViewById<Button>(R.id.btnImprimir).setOnClickListener {
            Toast.makeText(this, getString(R.string.archivo_descargado), Toast.LENGTH_SHORT).show()
        }

        // Configuración de botones de la pantalla
        findViewById<Button>(R.id.btnVolverInicio).setOnClickListener {
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