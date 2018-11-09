package br.com.rotacilio.mymoney.adapters.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.cards_list_item.view.*

class CardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val imgFlag = itemView.cardFlag!!
    val name = itemView.txtCardName!!
    val payDay = itemView.txtCardPayDay!!
    val status = itemView.txtCardStatus!!
    val btnDeleteCard = itemView.btnDeleteCategory!!

}