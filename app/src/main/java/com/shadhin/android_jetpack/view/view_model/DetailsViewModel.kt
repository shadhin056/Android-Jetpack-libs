package com.shadhin.android_jetpack.view.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shadhin.android_jetpack.view.model.DogModel

class DetailsViewModel : ViewModel() {
    val dogLiveData = MutableLiveData<DogModel>()
    fun fetch() {
        val dog = DogModel("1", "2", "3", "4", "5", "6", "7")
        dogLiveData.value = dog
    }
}