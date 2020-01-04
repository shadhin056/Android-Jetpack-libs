package com.shadhin.android_jetpack.view.view


import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import com.shadhin.android_jetpack.R
import com.shadhin.android_jetpack.databinding.FragmentDetailsBinding
import com.shadhin.android_jetpack.databinding.RowItemDogBinding
import com.shadhin.android_jetpack.databinding.SendSmsDialogBinding
import com.shadhin.android_jetpack.view.model.DogModel
import com.shadhin.android_jetpack.view.model.DogPalette
import com.shadhin.android_jetpack.view.model.SmsInfo
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
    private lateinit var dataBinding: FragmentDetailsBinding
    private var sendSmsStarted = false
    private var currentDog: DogModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)

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

            currentDog = dogs

            dogs?.let {
                dataBinding.dog = dogs
                it.imageUrl?.let {
                    setUpBackgroundColor(it)
                }
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

    private fun setUpBackgroundColor(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate { palette ->
                            val intColor = palette?.vibrantSwatch?.rgb ?: 0
                            val myPalatte = DogPalette(intColor)
                            dataBinding.palette = myPalatte
                        }
                }

            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send_sms -> {
                sendSmsStarted = true
                (activity as MainActivity).checkSMSPermission()
            }
            R.id.action_share -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted: Boolean) {

        if (sendSmsStarted && permissionGranted) {
            context?.let {
                val smsInfo = SmsInfo(
                    "",
                    "${currentDog?.dogBreed} bred for ${currentDog?.bredFor}",
                    currentDog?.imageUrl
                )
                val dialogBinding = DataBindingUtil.inflate<SendSmsDialogBinding>(
                    LayoutInflater.from(it),
                    R.layout.send_sms_dialog,
                    null,
                    false
                )
                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMS") { dialog, which ->
                        if (!dialogBinding.smsDestination.text.isNullOrEmpty()) {
                            smsInfo.to = dialogBinding.smsDestination.text.toString()
                            sendSms(smsInfo)
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, which ->

                    }.show()
                dialogBinding.smsinfo = smsInfo
            }
        }
    }

    private fun sendSms(smsinfo: SmsInfo) {
        val intent = Intent(context, MainActivity::class.java)
        val pi = PendingIntent.getActivity(context, 0, intent, 0)
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(smsinfo.to, null, smsinfo.text, pi, null)
    }
}
