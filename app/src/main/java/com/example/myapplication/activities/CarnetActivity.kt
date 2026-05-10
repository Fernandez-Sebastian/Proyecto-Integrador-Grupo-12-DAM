package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R

class CarnetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet)

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

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }

        // --- LÓGICA DEL FOOTER ---
        val llCentro = findViewById<LinearLayout>(R.id.ll_footer_centro)
        val ivHome = findViewById<ImageView>(R.id.iv_home)
        val ivSettings = findViewById<ImageView>(R.id.iv_settings)
        val ivLogout = findViewById<ImageView>(R.id.iv_logout)

        // En el Carnet: franja blanca y los 3 iconos
        llCentro.visibility = View.VISIBLE
        ivHome.visibility = View.VISIBLE
        ivSettings.visibility = View.VISIBLE
        ivLogout.visibility = View.VISIBLE

        // Navegación del footer
        ivHome.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        ivLogout.setOnClickListener {
            val intent = Intent(this, LogoutActivity::class.java)
            startActivity(intent)
        }
    }
}