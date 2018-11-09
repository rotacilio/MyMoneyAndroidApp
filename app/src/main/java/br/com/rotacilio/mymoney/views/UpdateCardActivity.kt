package br.com.rotacilio.mymoney.views

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import br.com.rotacilio.mymoney.R
import br.com.rotacilio.mymoney.adapters.MyFlagsSpinnerAdapter
import br.com.rotacilio.mymoney.commons.CallbackRequest
import br.com.rotacilio.mymoney.domain.Brand
import br.com.rotacilio.mymoney.domain.Card
import br.com.rotacilio.mymoney.requests.BrandsRequest
import br.com.rotacilio.mymoney.requests.CardsRequest
import kotlinx.android.synthetic.main.activity_update_card.*
import kotlinx.android.synthetic.main.form_card.*

class UpdateCardActivity : AppCompatActivity(), CallbackRequest, View.OnClickListener {

    companion object {
        val TAG = UpdateCardActivity::class.java.simpleName
    }

    private var mCard: Card? = null
    private var spinnerAdapter: MyFlagsSpinnerAdapter? = null
    private var selectedBrand: Brand? = null
    private var brands: MutableList<Brand?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_card)

        mCard = intent.extras.getSerializable("card_obj") as Card

        configureSpinnerBrands()
        loadData()

        btnUpdate.setOnClickListener(this)
        btnDeactivate.setOnClickListener(this)
    }

    private fun updateViewData() {
        inputName.setText(mCard?.name)
        inputPayDay.setText(mCard?.expirateDay.toString())
        val indexOfBrand = brands?.indexOf(mCard?.brand)
        spinnerFlags.setSelection(if (indexOfBrand!! < 0) 0 else indexOfBrand!!)
    }

    private fun loadData() {
        val request = BrandsRequest()
        request.findAll(this, this)
    }

    private fun configureSpinnerBrands() {
        spinnerAdapter = MyFlagsSpinnerAdapter(this)
        spinnerFlags.adapter = spinnerAdapter
        spinnerFlags.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(adapterView: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                selectedBrand = if (position == 0) null else brands!![position]
            }
        }
    }

    override fun success(response: Any) {
        brands = response as MutableList<Brand?>?
        brands?.add(0, null)
        spinnerAdapter?.mFlags = brands
        updateViewData()
    }

    override fun failure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnUpdate -> createNewCard(view)
            R.id.btnDeactivate -> createNewCard(view)
        }
    }

    private fun createNewCard(v: View?) {
        if (isValidData(v)) {
            val card = Card()
            card.name = inputName.text.toString().trim()
            card.expirateDay = inputPayDay.text.toString().trim().toInt()
            card.brand = selectedBrand
            val cardsRequest = CardsRequest()
            cardsRequest.createNewCard(card, object : CallbackRequest {
                override fun success(response: Any) {
                    Snackbar.make(v!!, R.string.success_message_create_new_card, Snackbar.LENGTH_SHORT).show()
                    finish()
                }
                override fun failure(message: String) {
                    Snackbar.make(v!!, message, Snackbar.LENGTH_SHORT).show()
                }
            }, this)
        }
    }

    private fun isValidData(v: View?): Boolean {
        when {
            inputName.text.isBlank() -> {
                inputName.error = getString(R.string.error_message_empty_card_name)
                inputName.requestFocus()
                return false
            }
            inputPayDay.text.isBlank() -> {
                inputPayDay.error = getString(R.string.error_message_empty_card_pay_day)
                inputPayDay.requestFocus()
                return false
            }
            spinnerFlags.selectedItemPosition == 0 -> {
                Snackbar.make(v!!, R.string.error_message_empty_card_brand, Snackbar.LENGTH_SHORT).show()
                return false
            }
            else -> return true
        }
    }
}