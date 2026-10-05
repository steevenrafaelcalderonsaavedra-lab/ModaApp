package com.example.modaap

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.modaap.databinding.ActivityLoginBinding
import com.example.modaap.data.DBHelper
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar base de datos
        dbHelper = DBHelper(this)


        binding.btnIngresar.setOnClickListener {

            val usuario =
                binding.etUsuario.text.toString().trim()

            val password =
                binding.etPassword.text.toString().trim()


            binding.tilUsuario.error = null
            binding.tilPassword.error = null

            var hayError = false


            if (usuario.isEmpty()) {

                binding.tilUsuario.error =
                    "Ingrese el usuario"

                hayError = true
            }


            if (password.isEmpty()) {

                binding.tilPassword.error =
                    "Ingrese la contraseña"

                hayError = true
            }


            if (hayError) {
                return@setOnClickListener
            }


            // VALIDAR DESDE SQLITE

            val valido =
                dbHelper.validarUsuario(
                    usuario,
                    password
                )


            if (valido) {

                val rol =
                    dbHelper.obtenerRol(usuario)


                Toast.makeText(
                    this,
                    "Bienvenido $usuario",
                    Toast.LENGTH_SHORT
                ).show()


                val intent =
                    Intent(
                        this,
                        MenuActivity::class.java
                    )


                intent.putExtra(
                    "usuario",
                    usuario
                )

                intent.putExtra(
                    "rol",
                    rol
                )


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


        // CLIENTE SIN LOGIN

        binding.btnCatalogo.setOnClickListener {

            val intent =
                Intent(
                    this,
                    CatalogoActivity::class.java
                )

            startActivity(intent)
        }
    }
}