package com.shadhin.android_jetpack.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.shadhin.android_jetpack.R
import kotlinx.android.synthetic.main.fragment_details.*

/**
 * A simple [Fragment] subclass.
 */
class DetailsFragment : Fragment() {

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
        arguments?.let {
            dogUuidThis=DetailsFragmentArgs.fromBundle(it).dogUuid
            tvDetails?.text=dogUuidThis.toString()
        }
        fABDetails.setOnClickListener {
            val action = DetailsFragmentDirections.actionDetailsFragmentToListFragment()
            Navigation.findNavController(it).navigate(action)
        }

    }


}
