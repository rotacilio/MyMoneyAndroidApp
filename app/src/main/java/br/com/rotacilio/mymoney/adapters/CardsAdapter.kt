package br.com.rotacilio.mymoney.adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.rotacilio.mymoney.R
import br.com.rotacilio.mymoney.adapters.holders.CardViewHolder
import br.com.rotacilio.mymoney.commons.CallbackRequest
import br.com.rotacilio.mymoney.domain.Card
import br.com.rotacilio.mymoney.enums.CardFlags
import br.com.rotacilio.mymoney.requests.CardsRequest
import br.com.rotacilio.mymoney.views.UpdateCardActivity
import com.squareup.picasso.Picasso

class CardsAdapter : RecyclerView.Adapter<CardViewHolder>() {

    var mCards = mutableListOf<Card>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cards_list_item, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int = mCards.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = mCards[position]
        Picasso.get()
                .load(card.brand?.imagePath)
                .placeholder(R.drawable.ic_card)
                .error(R.drawable.ic_card)
                .into(holder.imgFlag)
        holder.name.text = card.name
        holder.payDay.text = "${holder.itemView.context.getString(R.string.pay_day)} ${String.format("%02d", card.expirateDay)}"

        if (card.enabled!!) {
            holder.btnDeleteCard.visibility = View.VISIBLE
            holder.status.text = holder.itemView.context.getString(R.string.enabled)
            holder.status.setTextColor(holder.itemView.context.resources.getColor(R.color.colorGreenPrimaryDark))
        } else {
            holder.btnDeleteCard.visibility = View.GONE
            holder.status.text = holder.itemView.context.getString(R.string.disabled)
            holder.status.setTextColor(holder.itemView.context.resources.getColor(R.color.colorRedPrimaryDark))
        }
        holder.btnDeleteCard.setOnClickListener {
            showConfirmationDialog(holder.itemView.context, card)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, UpdateCardActivity::class.java)
            intent.putExtra("card_obj", card)
            it.context.startActivity(intent)
        }
    }

    private fun showConfirmationDialog(context: Context, card: Card) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.title_disable_card)
        builder.setMessage(R.string.message_disable_card_confirmation_dialog)
        builder.setPositiveButton(R.string.yes, object : DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                val request = CardsRequest()
                request.updateStatus(card, object : CallbackRequest {
                    override fun success(response: Any) {
                        val result = response as Card
                        result.let {
                            val index = mCards.indexOf(it)
                            mCards[index] = it
                            notifyItemChanged(index)
                            Toast.makeText(context, R.string.success_message_disable_card, Toast.LENGTH_SHORT).show()
                            return
                        }
                        Toast.makeText(context, R.string.unknown_error_message, Toast.LENGTH_SHORT).show()
                    }
                    override fun failure(message: String) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }, context)
            }
        })
        builder.setNegativeButton(R.string.no, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                p0?.dismiss()
            }
        })
        builder.create().show()
    }
}