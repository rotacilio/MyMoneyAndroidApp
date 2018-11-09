package br.com.rotacilio.mymoney.domain

import java.io.Serializable

class Card : Serializable {

    var id: Long? = null
    var name: String? = null
    var expirateDay: Int? = null
    var enabled: Boolean? = null
    var createdAt: String? = null
    var updatedAt: String? = null
    var brand: Brand? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Card

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }


}