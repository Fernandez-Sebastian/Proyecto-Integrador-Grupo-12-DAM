package com.example.myapplication.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import android.content.Intent
import android.widget.Button

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val btn = findViewById<Button>(R.id.btnCobrarActividad)

        btn.setOnClickListener {
            val intent = Intent(this, CobrarActividad::class.java)
            startActivity(intent)
        }
    }


}