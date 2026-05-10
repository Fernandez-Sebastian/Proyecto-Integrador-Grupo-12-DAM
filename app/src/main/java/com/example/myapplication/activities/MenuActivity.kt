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

        val tvSaludo = findViewById<TextView>(R.id.tvSaludo)
        val tvAlerta = findViewById<TextView>(R.id.tvAlerta)

        val btnCobrarActividad = findViewById<Button>(R.id.btnCobrarActividad)
        val btnCobrarCuota = findViewById<Button>(R.id.btnCobrarCuota)
        val btnVerVencimientos = findViewById<Button>(R.id.btnVerVencimientos)
        val btnRegistrarSocio = findViewById<Button>(R.id.btnRegistrarSocio)
        val btnSalir = findViewById<Button>(R.id.btnSalir)
        val btnCarnet= findViewById<Button>(R.id.btnCarnet)

        // Traemos el Usuario desde el login para mostrarlo
        val nombreUsuario = intent.getStringExtra("usuario")
        tvSaludo.text = "Hola, $nombreUsuario"

        // Alert de Vencimiento
        // to do Manejo de la variable hayVencimientos por medio de una consulta a la base de datos
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

        btnCobrarActividad.setOnClickListener {
            val intent = Intent(this, CobrarActividad::class.java)
            startActivity(intent)
        }

        //Registrar No Socios
        val btnRegistrarNoSocio = findViewById<Button>(R.id.btnRegistrarNoSocio)

        btnRegistrarNoSocio.setOnClickListener {
            val intent = Intent(this, RegistrarNoSocioActivity::class.java)
            startActivity(intent)
        }

        btnCobrarCuota.setOnClickListener {
            val intent = Intent(this, CobrarCuotaActivity::class.java)
            startActivity(intent)
        }

        btnVerVencimientos.setOnClickListener {
            val intent = Intent(this, VencimientosActivity::class.java)
            startActivity(intent)
        }

        btnRegistrarSocio.setOnClickListener {
            val intent = Intent(this, RegistrarSocioActivity::class.java)
            startActivity(intent)
        }

        btnSalir.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
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