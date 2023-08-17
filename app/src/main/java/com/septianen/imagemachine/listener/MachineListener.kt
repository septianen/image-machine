package com.septianen.imagemachine.listener

interface MachineListener {

    fun onItemClicked(position: Int)

    fun onItemLongClicked(positions: List<Int>)
}