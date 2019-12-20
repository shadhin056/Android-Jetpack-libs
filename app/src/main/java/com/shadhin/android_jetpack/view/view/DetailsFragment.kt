package com.shadhin.android_jetpack.view.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

import com.shadhin.android_jetpack.R
import com.shadhin.android_jetpack.view.view_model.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_details.*

/**
 * A simple [Fragment] subclass.
 */
class DetailsFragment : Fragment() {
    private lateinit var viewModel: DetailsViewModel
    private var dogUuidThis = 0;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        viewModel.fetch()
        arguments?.let {
            dogUuidThis = DetailsFragmentArgs.fromBundle(
                it
            ).dogUuid
        }
        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.dogLiveData.observe(this, Observer { dogs ->
            dogs?.let {
                txtDogName.text = dogs.dogBreed
                txtDogPurpose.text = dogs.bredFor
                txtDogTemperament.text = dogs.temperament
                txtDogLifeSpan.text = dogs.lifeSpan
            }
        })
    }


}
