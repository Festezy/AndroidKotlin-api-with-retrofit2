package com.example.aplikasidenganapi

import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @GET("/latihanAPI/public/api/ceo")
    fun getCEOs(): Call<ArrayList<CEOModel>>

    @POST("/latihanAPI/public/api/ceo")
    fun addCEO(@Body newCEOModel: CEOModel) : Call<CEOModel>

    @DELETE("/latihan/public/api/ceo/{id}")
    fun deleteCEO(@Path("id") id: Int) : Call<CEOModel>
}