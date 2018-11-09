package br.com.rotacilio.mymoney.requests

import android.content.Context
import br.com.rotacilio.mymoney.commons.CallbackRequest
import br.com.rotacilio.mymoney.domain.Card
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardsRequest : BaseRequest() {

    fun findAll(refresh: Boolean, callbackRequest: CallbackRequest, context: Context) {
        super.call(callbackRequest, context)
        val call = mCardsServices?.findAll(if (refresh) "no-cache" else null)
        call?.enqueue(object : Callback<List<Card>> {
            override fun onFailure(call: Call<List<Card>>, t: Throwable) {
                mCallbackRequest?.failure(t.message!!)
            }

            override fun onResponse(call: Call<List<Card>>, response: Response<List<Card>>) {
                if (!response.isSuccessful) {
                    mCallbackRequest?.failure("Error")
                } else {
                    mCallbackRequest?.success(response.body()!!)
                }
            }
        })
    }

    fun updateStatus(card: Card, callbackRequest: CallbackRequest, context: Context) {
        super.call(callbackRequest, context)
        card.enabled = !card.enabled!!
        val call = mCardsServices?.updateCard(card)
        call?.enqueue(object : Callback<Card> {
            override fun onFailure(call: Call<Card>, t: Throwable) {
                mCallbackRequest?.failure(t.message!!)
            }

            override fun onResponse(call: Call<Card>, response: Response<Card>) {
                if (!response.isSuccessful) {
                    mCallbackRequest?.failure("Error")
                } else {
                    mCallbackRequest?.success(response.body()!!)
                }
            }
        })
    }

    fun createNewCard(card: Card, callbackRequest: CallbackRequest, context: Context) {
        super.call(callbackRequest, context)
        val call = mCardsServices?.createNewCard(card)
        call?.enqueue(object : Callback<Card> {
            override fun onFailure(call: Call<Card>, t: Throwable) {
                mCallbackRequest?.failure(t.message!!)
            }
            override fun onResponse(call: Call<Card>, response: Response<Card>) {
                if (!response.isSuccessful) {
                    mCallbackRequest?.failure("Error")
                } else {
                    mCallbackRequest?.success(response.body()!!)
                }
            }
        })
    }

    fun updateCard(card: Card, callbackRequest: CallbackRequest, context: Context) {
        super.call(callbackRequest, context)
        val call = mCardsServices?.updateCard(card)
        call?.enqueue(object : Callback<Card> {
            override fun onFailure(call: Call<Card>, t: Throwable) {
                mCallbackRequest?.failure(t.message!!)
            }
            override fun onResponse(call: Call<Card>, response: Response<Card>) {
                if (!response.isSuccessful) {
                    mCallbackRequest?.failure("Error")
                } else {
                    mCallbackRequest?.success(response.body()!!)
                }
            }
        })
    }
}