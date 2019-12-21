package com.shadhin.android_jetpack.view.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.shadhin.android_jetpack.R
import com.shadhin.android_jetpack.view.model.DogModel
import com.shadhin.android_jetpack.view.util.getProgressDrawable
import com.shadhin.android_jetpack.view.util.loadImage
import kotlinx.android.synthetic.main.row_item_dog.view.*

class DogsListAdapter(val doglist: ArrayList<DogModel>) :
    RecyclerView.Adapter<DogsListAdapter.DogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_item_dog, parent, false);
        return DogViewHolder(view)
    }

    override fun getItemCount() = doglist.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.name.text = doglist[position].dogBreed
        holder.view.lifeSpan.text = doglist[position].lifeSpan
        holder.view.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToDetailsFragment()
            action.dogUuid = doglist[position].uuid
            Navigation.findNavController(it).navigate(action)
        }
        holder.view.imageview.loadImage(
            doglist[position].imageUrl,
            getProgressDrawable(holder.view.imageview.context)
        )
    }

    fun updateDogList(newDogsList: List<DogModel>) {
        doglist.clear()
        doglist.addAll(newDogsList)
        notifyDataSetChanged()
    }

    class DogViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

    }
}