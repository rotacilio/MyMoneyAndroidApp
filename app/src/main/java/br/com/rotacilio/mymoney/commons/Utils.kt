package br.com.rotacilio.mymoney.commons

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.widget.DialogTitle
import br.com.rotacilio.mymoney.listeners.OnConfirmationDialogSelectionListener

class Utils {

    companion object {
        fun showProgressDialog(context: Context, message: String): ProgressDialog {
            return ProgressDialog.show(context, null, message, true, false)
        }

        fun dismissProgressDialog(progressDialog: ProgressDialog?) {
            if (progressDialog != null && progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        }

        fun showConfirmationDialog(context: Context, title: String, message: String, listener: OnConfirmationDialogSelectionListener) {

        }
    }
}