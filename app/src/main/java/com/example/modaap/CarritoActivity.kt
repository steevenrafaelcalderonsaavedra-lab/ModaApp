package com.example.modaap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.modaap.databinding.ActivityCarritoBinding

class CarritoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCarritoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}