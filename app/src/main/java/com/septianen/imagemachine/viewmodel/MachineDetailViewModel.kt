package com.septianen.imagemachine.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septianen.imagemachine.constant.Message
import com.septianen.imagemachine.model.Image
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
    val imagesLiveData: MutableLiveData<Resource<List<Image>>> = MutableLiveData()
    val saveItemLiveData: MutableLiveData<Resource<String>> = MutableLiveData()

    private lateinit var machine: Machine
    private lateinit var imagePaths: List<Image>

    init {

        getMachines()
    }

    private fun getMachines() = viewModelScope.launch(Dispatchers.IO) {
        machine = Temporary.machine?.copy() ?: Machine()
        getImages()

        machineLiveData.postValue(Resource.Success(machine))
    }

    private fun getImages() {

        imagePaths = machine.id?.let { repository.getImages(it) } ?: ArrayList()

        imagesLiveData.postValue(Resource.Success(imagePaths))
    }

    fun saveData(machine: Machine, imagePaths: List<Image>?) = viewModelScope.launch(Dispatchers.IO) {

        when {
            machine.name.isNullOrEmpty() -> postErrorValidaton(Message.EMPTY_MACHINE_NAME)
            machine.type.isNullOrEmpty() -> postErrorValidaton(Message.EMPTY_MACHINE_TYPE)
            machine.qrNumber == null -> postErrorValidaton(Message.EMPTY_MACHINE_NUMBER)
            machine.date == null ->
                postErrorValidaton(Message.EMPTY_MACHINE_DATE)
            imagePaths.isNullOrEmpty() -> postErrorValidaton(Message.EMPTY_IMAGES)
            else -> {

                this@MachineDetailViewModel.imagePaths = imagePaths

                // Save Machine
                saveMachine()

                saveItemLiveData.postValue(Resource.Success(Message.SUCCESS_SAVE_DATA))
            }
        }
    }

    private fun saveMachine() = viewModelScope.launch(Dispatchers.IO) {
        if (machine.thumbnail.isNullOrEmpty()) {
            machine.thumbnail = imagePaths[0].imagePath
        }
        machine.id = repository.upsertMachine(machine)
        this@MachineDetailViewModel.machine = machine
        Temporary.machine = machine.copy()

        // Save Images
        saveImages(imagePaths)

        machineLiveData.postValue(Resource.Success(machine))
    }

    private fun saveImages(imagePaths: List<Image>) = viewModelScope.launch(Dispatchers.IO) {
        this@MachineDetailViewModel.imagePaths = imagePaths
        val images = ArrayList<Image>()

        Log.d("TAG", "VM saveImages: imagePaths = ${imagePaths.size}")

//        for (path in imagePaths) {
//            images.add(
//                Image(
//                    machineId = machine.id,
//                    imagePath = path,
//                )
//            )
//        }

        imagePaths.map { it.copy(machineId = machine.id) }

        imagePaths.forEach {
            it.machineId = machine.id
        }

        machine.id?.let { repository.updateImages(it, imagePaths) }

        Log.d("TAG", "VM saveImages: images = ${images.size}")

        imagesLiveData.postValue(Resource.Success(imagePaths))
    }

    private fun postErrorValidaton(message: String) {
        saveItemLiveData.postValue(Resource.Error(message))
    }

    fun deleteData(machine: Machine, images: List<Image>) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteImages(images)
        repository.deleteMachine(machine)
    }

    fun countMaximumImage(selctedImage: Int, savedImage: Int): Int {

        return minOf(selctedImage, (10 - savedImage))
    }
}