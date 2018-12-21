package br.com.rotacilio.mymoney.listeners

import android.content.DialogInterface

interface OnConfirmationDialogSelectionListener {
    fun yes(dialogInterface: DialogInterface, which: Int)
    fun no(dialogInterface: DialogInterface, which: Int)
}