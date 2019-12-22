package com.shadhin.android_jetpack.view.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

import com.shadhin.android_jetpack.R
import com.shadhin.android_jetpack.databinding.FragmentDetailsBinding
import com.shadhin.android_jetpack.databinding.RowItemDogBinding
import com.shadhin.android_jetpack.view.util.getProgressDrawable
import com.shadhin.android_jetpack.view.util.loadImage
import com.shadhin.android_jetpack.view.view_model.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_details.*

/**
 * A simple [Fragment] subclass.
 */
class DetailsFragment : Fragment() {
    private lateinit var viewModel: DetailsViewModel
    private var dogUuidThis = 0;
    private lateinit var dataBinding:FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_details,container,false)

        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_details, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)

        arguments?.let {
            dogUuidThis = DetailsFragmentArgs.fromBundle(
                it
            ).dogUuid
        }
        viewModel.fetch(dogUuidThis);
        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.dogLiveData.observe(this, Observer { dogs ->
            dogs?.let {
              dataBinding.dog=dogs
                /*  txtDogName.text = dogs.dogBreed
                txtDogPurpose.text = dogs.bredFor
                txtDogTemperament.text = dogs.temperament
                txtDogLifeSpan.text = dogs.lifeSpan
                context?.let {
                    (ivDogImage.loadImage(dogs.imageUrl, getProgressDrawable(it)))
                }*/
            }
        })
    }


}
