package com.example.modaap

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.modaap.adapter.RopaAdapter
import com.example.modaap.data.RopaDao
import com.example.modaap.databinding.ActivityRopaBinding
import androidx.core.widget.doAfterTextChanged
class RopaActivity : AppCompatActivity() {
    private lateinit var binding:
            ActivityRopaBinding

    private lateinit var ropaDao:
            RopaDao

    private lateinit var adapter:
            RopaAdapter


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityRopaBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)


        ropaDao =
            RopaDao(this)


        adapter =
            RopaAdapter(
                emptyList()
            ) { ropa ->

                val intent =
                    Intent(
                        this,
                        RegistrarRopaActivity::class.java
                    )

                intent.putExtra(
                    "id",
                    ropa.id
                )

                startActivity(intent)
            }

        binding.rvRopa.layoutManager =
            LinearLayoutManager(this)


        binding.rvRopa.adapter =
            adapter


        binding.btnNuevaRopa
            .setOnClickListener {

                val intent =
                    Intent(
                        this,
                        RegistrarRopaActivity::class.java
                    )

                startActivity(intent)
            }
        binding.etBuscar.doAfterTextChanged {

            val texto =
                it.toString().trim()

            buscarRopa(texto)
        }
    }


    override fun onResume() {

        super.onResume()

        cargarRopa()
    }
    private fun buscarRopa(
        texto: String
    ) {

        val lista =
            ropaDao.listar(texto)

        adapter.actualizar(lista)
    }

    private fun cargarRopa() {

        val lista =
            ropaDao.listar()

        adapter.actualizar(lista)
    }
}