package com.example.modaap

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.modaap.databinding.ActivityCatalogoBinding

class CatalogoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCatalogoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnCarrito.setOnClickListener {

            val intent = Intent(
                this,
                CarritoActivity::class.java
            )

            startActivity(intent)
        }
    }
}