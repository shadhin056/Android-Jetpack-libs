package com.shadhin.android_jetpack.view.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shadhin.android_jetpack.view.model.DogModel

class ListViewModel : ViewModel() {
    val dogs = MutableLiveData<List<DogModel>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val dogsloading = MutableLiveData<Boolean>()

    fun refresh() {
        val dog1 = DogModel("1", "1", "1", "1", "1", "1", "1")
        val dog2 = DogModel("2", "2", "2", "2", "2", "2", "2")
        val dog3 = DogModel("3", "3", "3", "3", "3", "3", "3")
        val dogList = arrayListOf<DogModel>(dog1, dog2, dog3)
        dogs.value = dogList
        dogsLoadError.value = false
        dogsloading.value = false
    }
}
