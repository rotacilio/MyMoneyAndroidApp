package br.com.rotacilio.mymoney.commons

interface CallbackRequest {

    fun success(response: Any)
    fun failure(message: String)
}