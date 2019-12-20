package com.shadhin.android_jetpack.view.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.shadhin.android_jetpack.R
import com.shadhin.android_jetpack.view.view_model.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val mAdapter = DogsListAdapter(arrayListOf())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()
        rvDogList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        srlRefreshLayout.setOnRefreshListener {
            rvDogList.visibility=View.GONE
            tvError.visibility=View.GONE
            pBLoading.visibility=View.VISIBLE
            viewModel.refresh()
            srlRefreshLayout.isRefreshing=false
        }
        obserViewModel()
    }

    private fun obserViewModel() {
        viewModel.dogs.observe(this, Observer { dogs ->
            dogs?.let {
                rvDogList.visibility = View.VISIBLE
                mAdapter.updateDogList(dogs)
            }
        })
        viewModel.dogsLoadError.observe(this, Observer { isError ->
            tvError.visibility = if (isError) View.VISIBLE else View.GONE
        })
        viewModel.dogsloading.observe(this, Observer { isLoading ->
            isLoading?.let {
                pBLoading.visibility = if (it) View.VISIBLE else View.GONE

                if (it) {
                    tvError.visibility = View.GONE
                    rvDogList.visibility = View.GONE
                }
            }

        })

    }
}
