package br.com.rotacilio.mymoney.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Card : Serializable {

    var id: String? = null
    var name: String? = null
    var expirateDay: Int? = null
    var enabled: Boolean? = null
    var createdAt: String? = null
    var updatedAt: String? = null
    var brand: Brand? = null

}