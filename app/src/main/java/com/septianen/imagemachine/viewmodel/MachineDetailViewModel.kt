package com.septianen.imagemachine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septianen.imagemachine.constant.Message
import com.septianen.imagemachine.model.Machine
import com.septianen.imagemachine.model.Temporary
import com.septianen.imagemachine.repository.MachineRepository
import com.septianen.imagemachine.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineDetailViewModel @Inject constructor(
    private val repository: MachineRepository
): ViewModel() {

    val machineLiveData: MutableLiveData<Resource<Machine>> = MutableLiveData()

    private lateinit var machine: Machine

    init {

        getMachines()
    }

    private fun getMachines() = viewModelScope.launch(Dispatchers.IO) {
        machine = Temporary.machine?.copy() ?: Machine()

        machineLiveData.postValue(Resource.Success(machine))
    }

    fun saveData(machine: Machine) = viewModelScope.launch(Dispatchers.IO) {

        when {
            machine.name.isNullOrEmpty() -> postErrorValidaton(Message.EMPTY_MACHINE_NAME)
            machine.type.isNullOrEmpty() -> postErrorValidaton(Message.EMPTY_MACHINE_TYPE)
            else -> {
                machine.id = repository.upsertMachine(machine)

                this@MachineDetailViewModel.machine = machine
                Temporary.machine = machine.copy()

                machineLiveData.postValue(Resource.Success(machine))
            }
        }
    }

    private fun postErrorValidaton(message: String) {
        machineLiveData.postValue(Resource.Error(message))
    }
}