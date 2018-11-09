package br.com.rotacilio.mymoney.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import br.com.rotacilio.mymoney.R
import br.com.rotacilio.mymoney.domain.Brand
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.flags_spinner_dropdown_item.view.*

class MyFlagsSpinnerAdapter(context: Context) :
        BaseAdapter() {

    private val mInflater = LayoutInflater.from(context)!!
    var mFlags: MutableList<Brand?>? = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getCount(): Int = mFlags?.size!!

    override fun getItem(position: Int): Brand? = mFlags!![position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getCustomView(position, convertView, parent!!)
    }

    @SuppressLint("ResourceAsColor")
    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val vh: RowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.flags_spinner_dropdown_item, parent, false)
            vh = RowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as RowHolder
        }

        if (position == 0) {
            vh.flagImage.visibility = View.GONE
            vh.flagName.setText(R.string.select_a_flag)
            vh.flagName.setTextColor(R.color.colorTextBlack54)
        } else {
            vh.flagImage.visibility = View.VISIBLE
            vh.flagName.setTextColor(R.color.colorTextBlack87)
            vh.flagName.text = mFlags!![position]?.name
            Picasso.get()
                    .load(mFlags!![position]?.imagePath)
                    .placeholder(R.drawable.ic_card)
                    .error(R.drawable.ic_card)
                    .into(vh.flagImage)
        }
        return view
    }

    private class RowHolder(row: View?) {
        var flagImage: ImageView = row!!.flagImage
        var flagName: TextView = row!!.flagName
    }
}