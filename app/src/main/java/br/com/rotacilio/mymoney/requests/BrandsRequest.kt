package br.com.rotacilio.mymoney.requests

import android.content.Context
import br.com.rotacilio.mymoney.commons.CallbackRequest
import br.com.rotacilio.mymoney.domain.Brand
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BrandsRequest : BaseRequest() {

    fun findAll(callbackRequest: CallbackRequest, context: Context) {
        super.call(callbackRequest, context)
        val call = mBrandsServices?.findAll()
        call?.enqueue(object : Callback<List<Brand>> {
            override fun onFailure(call: Call<List<Brand>>, t: Throwable) {
                mCallbackRequest?.failure(t.message!!)
            }
            override fun onResponse(call: Call<List<Brand>>, response: Response<List<Brand>>) {
                if (!response.isSuccessful) {
                    mCallbackRequest?.failure("Erro ao tentar listar as bandeiras dos cartões")
                } else {
                    mCallbackRequest?.success(response.body()!!)
                }
            }
        })
    }
}