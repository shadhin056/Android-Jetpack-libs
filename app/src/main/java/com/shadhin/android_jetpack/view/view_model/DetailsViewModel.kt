package com.shadhin.android_jetpack.view.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shadhin.android_jetpack.view.model.DogDatabase
import com.shadhin.android_jetpack.view.model.DogModel
import kotlinx.coroutines.launch

class DetailsViewModel (application:Application): BaseViewModel(application) {
    val dogLiveData = MutableLiveData<DogModel>()
    fun fetch(uuid :Int) {
        launch {
            val dog =DogDatabase(getApplication()).dogDao().getDog(uuid)
            dogLiveData.value = dog
        }

    }
}