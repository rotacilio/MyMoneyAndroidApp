package br.com.rotacilio.mymoney.views

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import br.com.rotacilio.mymoney.R
import br.com.rotacilio.mymoney.adapters.MyFlagsSpinnerAdapter
import br.com.rotacilio.mymoney.commons.CallbackRequest
import br.com.rotacilio.mymoney.commons.Utils
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
    private var progressDialog: ProgressDialog? = null

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
        Log.d(TAG, "enabled: ${mCard?.enabled!!}")
        if (mCard?.enabled!!) {
            btnDeactivate.setBackgroundResource(R.drawable.bg_btn_deactivate)
            btnDeactivate.text = getString(R.string.deactive)
        } else {
            btnDeactivate.setBackgroundResource(R.drawable.bg_btn_enable)
            btnDeactivate.text = getString(R.string.active)
        }
    }

    private fun loadData() {
        progressDialog = Utils.showProgressDialog(this, getString(R.string.loading_brands))
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
        Utils.dismissProgressDialog(progressDialog)
    }

    override fun failure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Utils.dismissProgressDialog(progressDialog)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnUpdate -> updateCard(view)
            R.id.btnDeactivate -> updateStatus()
        }
    }

    private fun updateStatus() {
        showConfirmationDialog(this, mCard!!)
    }

    private fun updateCard(v: View?) {
        if (isValidData(v)) {
            progressDialog = Utils.showProgressDialog(this, getString(R.string.updating_card))
            mCard?.name = inputName.text.toString().trim()
            mCard?.expirateDay = inputPayDay.text.toString().trim().toInt()
            mCard?.brand = selectedBrand
            val cardsRequest = CardsRequest()
            cardsRequest.updateCard(mCard!!, object : CallbackRequest {
                override fun success(response: Any) {
                    Toast.makeText(baseContext, R.string.success_message_update_card, Toast.LENGTH_SHORT).show()
                    Utils.dismissProgressDialog(progressDialog)
                    finish()
                }
                override fun failure(message: String) {
                    Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
                    Utils.dismissProgressDialog(progressDialog)
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

    private fun showConfirmationDialog(context: Context, card: Card) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(if (card.enabled!!) R.string.title_disable_card else R.string.title_enable_card)
        builder.setMessage(if (card.enabled!!) R.string.message_disable_card_confirmation_dialog else R.string.message_enable_card_confirmation_dialog)
        builder.setPositiveButton(R.string.yes, object : DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                val request = CardsRequest()
                request.updateStatus(card, object : CallbackRequest {
                    override fun success(response: Any) {
                        val result = response as Card
                        result.let {
                            mCard = it
                            updateViewData()
                            Toast.makeText(context,
                                    if (it.enabled!!) R.string.success_message_enable_card else R.string.success_message_disable_card,
                                    Toast.LENGTH_SHORT).show()
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