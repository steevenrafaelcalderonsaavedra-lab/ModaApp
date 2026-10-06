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

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // HU-13: comprobar si ya existe una sesión
        verificarSesion()

        // INGRESAR COMO ADMINISTRADOR
        binding.btnIngresar.setOnClickListener {
            iniciarSesion()
        }

        // VER CATÁLOGO COMO CLIENTE
        // En tu XML se llama btnCatalogo
        binding.btnCatalogo.setOnClickListener {

            val intent = Intent(
                this,
                CatalogoActivity::class.java
            )

            startActivity(intent)
        }
    }

    // ==========================================
    // INICIAR SESIÓN
    // ==========================================

    private fun iniciarSesion() {

        val usuario = binding.etUsuario
            .text
            .toString()
            .trim()

        val password = binding.etPassword
            .text
            .toString()
            .trim()

        // Limpiar errores
        binding.etUsuario.error = null
        binding.etPassword.error = null

        // Validar usuario
        if (usuario.isEmpty()) {

            binding.etUsuario.error =
                "Ingrese el usuario"

            binding.etUsuario.requestFocus()
            return
        }

        // Validar contraseña
        if (password.isEmpty()) {

            binding.etPassword.error =
                "Ingrese la contraseña"

            binding.etPassword.requestFocus()
            return
        }

        // Credenciales del administrador
        if (
            usuario == "admin" &&
            password == "1234"
        ) {

            // Guardar sesión
            guardarSesion()

            val intent = Intent(
                this,
                MenuActivity::class.java
            )

            // Limpiar historial
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            finish()

        } else {

            Toast.makeText(
                this,
                "Credenciales incorrectas",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // ==========================================
    // GUARDAR SESIÓN
    // ==========================================

    private fun guardarSesion() {

        val preferencias =
            getSharedPreferences(
                "ModaAppPrefs",
                MODE_PRIVATE
            )

        preferencias
            .edit()
            .putBoolean(
                "sesion_admin",
                true
            )
            .apply()
    }

    // ==========================================
    // VERIFICAR SESIÓN
    // ==========================================

    private fun verificarSesion() {

        val preferencias =
            getSharedPreferences(
                "ModaAppPrefs",
                MODE_PRIVATE
            )

        val sesionActiva =
            preferencias.getBoolean(
                "sesion_admin",
                false
            )

        if (sesionActiva) {

            val intent = Intent(
                this,
                MenuActivity::class.java
            )

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            finish()
        }
    }
}