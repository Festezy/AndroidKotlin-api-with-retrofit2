package com.example.aplikasidenganapi

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListOfDataIntoRecyclerView()
        btnAddRecord.setOnClickListener {
            addRecord()
        }
    }

    private fun setupListOfDataIntoRecyclerView() {
        rvItemList.layoutManager = LinearLayoutManager(this)

        //Ambil data CEO dari Api
        var apiInterface: ApiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)
        apiInterface.getCEOs().enqueue(object : Callback<ArrayList<CEOModel>> {
            override fun onFailure(call: Call<ArrayList<CEOModel>>?, t: Throwable) {
                Toast.makeText(baseContext, "Data downloading is failed", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ArrayList<CEOModel>>?, response: Response<ArrayList<CEOModel>>?){
                var ceoData = response?.body()!!
                if(ceoData.size > 0){
                    rvItemList.visibility = View.VISIBLE
                    tvNoRecordAvailable.visibility = View.GONE
                    rvItemList.adapter = MyAdapter(this@MainActivity, ceoData)
                } else {
                    rvItemList.visibility = View.GONE
                    tvNoRecordAvailable.visibility = View.VISIBLE
                }
                Toast.makeText(baseContext, "Data downloading is success", Toast.LENGTH_LONG).show()
            }
        })
    }
    // Method to add data
    fun addRecord(){
        val name = etName.text.toString()
        val companyName = etCompanyName.text.toString()

        if(name == "" || companyName == ""){
            Toast.makeText(this, "Masih ada field yg kosong, tolong diisi", Toast.LENGTH_LONG).show()
        } else {
            val newCEO : CEOModel = CEOModel(null, name, companyName)

            var apiInterface: ApiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)

            var requestCall : Call<CEOModel> = apiInterface.addCEO(newCEO)

            requestCall.enqueue(object : Callback<CEOModel>{
                override fun onFailure(call: Call<CEOModel>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Gagal tersimpan", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(call: Call<CEOModel>, response: Response<CEOModel>) {
                    if (response.isSuccessful){
                        Toast.makeText(this@MainActivity, "Berhasil tersimpan",Toast.LENGTH_LONG).show()
                        setupListOfDataIntoRecyclerView()
                        etName.setText("")
                        etCompanyName.setText("")
                    } else {
                        Toast.makeText(this@MainActivity, "Gagal tersimpan", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
    }

    //method to show delete alert (untuk menampilkan dialog delete)
    fun deleteRecordAlertDialog(CEOModel: CEOModel) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")

        builder.setMessage("Are you sure?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){ dialog: DialogInterface?, which: Int ->
            var apiInterface: ApiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)

            var requestCall : Call<CEOModel> = apiInterface.deleteCEO(CEOModel.id!!)
                requestCall.enqueue(object : Callback<CEOModel>{
                    override fun onResponse(call: Call<CEOModel>, response: Response<CEOModel>) {
                        if (response.isSuccessful){
                            Toast.makeText(this@MainActivity, "Berhasil Tersimpanh", Toast.LENGTH_LONG).show()
                            setupListOfDataIntoRecyclerView()
                            etName.setText("")
                            etCompanyName.setText("")
                        } else {
                            Toast.makeText(this@MainActivity, "gk jadi succes", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<CEOModel>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Gagal Terhapus", Toast.LENGTH_LONG).show()
                    }
                })
        }
        builder.setNegativeButton("No"){dialog: DialogInterface?, which: Int ->
//            dialog.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    fun updateRecordDialog(item: CEOModel) {

    }
}