package com.example.modaap

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.modaap.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Activamos ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // BOTÓN INGRESAR COMO ADMINISTRADOR
        binding.btnIngresar.setOnClickListener {

            val usuario = binding.etUsuario.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Limpiamos errores anteriores
            binding.tilUsuario.error = null
            binding.tilPassword.error = null

            var hayError = false


            // VALIDAR USUARIO VACÍO
            if (usuario.isEmpty()) {

                binding.tilUsuario.error = "Ingrese el usuario"

                hayError = true
            }


            // VALIDAR CONTRASEÑA VACÍA
            if (password.isEmpty()) {

                binding.tilPassword.error = "Ingrese la contraseña"

                hayError = true
            }


            // Si hay campos vacíos, detenemos el proceso
            if (hayError) {
                return@setOnClickListener
            }


            // VALIDACIÓN TEMPORAL DEL SPRINT 1
            if (usuario == "admin" && password == "1234") {

                Toast.makeText(
                    this,
                    "Bienvenido administrador",
                    Toast.LENGTH_SHORT
                ).show()


                // Abrimos el menú
                val intent = Intent(
                    this,
                    MenuActivity::class.java
                )

                startActivity(intent)


                // Cerramos LoginActivity
                // para que atrás no vuelva al login
                finish()

            } else {

                // Credenciales incorrectas

                Toast.makeText(
                    this,
                    "Credenciales incorrectas",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        // BOTÓN VER CATÁLOGO - CLIENTE
        binding.btnCatalogo.setOnClickListener {

            val intent = Intent(
                this,
                CatalogoActivity::class.java
            )

            startActivity(intent)
        }
    }
}