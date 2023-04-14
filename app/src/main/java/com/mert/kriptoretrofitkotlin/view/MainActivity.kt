package com.mert.kriptoretrofitkotlin.view

import RecyclerViewAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mert.kriptoretrofitkotlin.databinding.ActivityMainBinding
import com.mert.kriptoretrofitkotlin.model.CryptoModel
import com.mert.kriptoretrofitkotlin.service.CryptoApı
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(),RecyclerViewAdapter.Listener {
    private val Base_URL = "https://raw.githubusercontent.com/"
    private var cryptoModels : ArrayList<CryptoModel> ?  = null
    private lateinit var binding: ActivityMainBinding
    private var recyclerviewAdapter : RecyclerViewAdapter ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val layoutmanager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recylerView.layoutManager = layoutmanager

        loadData()
    }
    private fun loadData(){
        val retrofit = Retrofit.Builder().baseUrl(Base_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(CryptoApı::class.java)
        val call = service.getData()
        call.enqueue(object : Callback<List<CryptoModel>>{
            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {

                        cryptoModels = ArrayList(it)
                        recyclerviewAdapter = RecyclerViewAdapter(cryptoModels!!, this@MainActivity)
                        binding.recylerView.adapter = recyclerviewAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })

    }

    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(this,"Clicked ${cryptoModel.currency}",Toast.LENGTH_LONG).show()
    }
}