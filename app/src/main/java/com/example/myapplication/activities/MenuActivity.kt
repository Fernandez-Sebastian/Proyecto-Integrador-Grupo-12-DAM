package com.example.myapplication.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val btn = findViewById<Button>(R.id.btnCobrarActividad)
        val tvSaludo = findViewById<TextView>(R.id.tvSaludo)
        val btnCarnet= findViewById<Button>(R.id.btnCarnet)

        // Traemos el Usuario desde el login para mostrarlo
        val nombreUsuario = intent.getStringExtra("usuario")
        tvSaludo.text = "Hola, $nombreUsuario"

        val tvAlerta = findViewById<TextView>(R.id.tvAlerta)

        // Alert de Vencimiento
        val hayVencimientos = false

        if (hayVencimientos) {
            tvAlerta.text = "Tenés socios con vencimientos hoy"
            tvAlerta.setBackgroundResource(R.color.fondo_alerta_roja)
            tvAlerta.setTextColor(getColor(R.color.rojo_alerta))
        } else {
            tvAlerta.text = "No hay socios con vencimientos hoy"
            tvAlerta.setBackgroundResource(R.color.fondo_alerta_verde)
            tvAlerta.setTextColor(getColor(R.color.verde_alerta))
        }

        btn.setOnClickListener {
            val intent = Intent(this, CobrarActividad::class.java)
            startActivity(intent)
        }

        btnCarnet.setOnClickListener {
            val intent = Intent(this, CarnetActivity::class.java)
            intent.putExtra("usuario", nombreUsuario)
            startActivity(intent)
        }

        // --- LÓGICA DEL FOOTER ---
        val llCentro = findViewById<LinearLayout>(R.id.ll_footer_centro)
        val ivHome = findViewById<ImageView>(R.id.iv_home)
        val ivSettings = findViewById<ImageView>(R.id.iv_settings)
        val ivLogout = findViewById<ImageView>(R.id.iv_logout)

        // Mostrar la franja blanca
        llCentro.visibility = View.VISIBLE
        
        // En el Menú: No aparece Home. Aparecen Settings y Logout.
        ivHome.visibility = View.GONE
        ivSettings.visibility = View.VISIBLE
        ivLogout.visibility = View.VISIBLE

        ivLogout.setOnClickListener {
            val intent = Intent(this, LogoutActivity::class.java)
            startActivity(intent)
        }
    }
}