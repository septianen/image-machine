package com.septianen.imagemachine.listener

interface DialogListener {

    fun onCloseDialog(requestCode: Int)

    fun onButtonClicked(requestCode: Int)
}