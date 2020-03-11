package com.jinesh.mpokket.extension

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jinesh.mpokket.util.DebounceTextWatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


fun logv(messsage: String){
    Log.v("mvvm_log--->",messsage)
}

fun Context.toast(messsage: String,isLongToast:Boolean = true) {
    Toast.makeText(this, messsage, if (isLongToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
        .show()
}


fun AppCompatEditText.setDebounce(f: () -> Unit) {
    this.addTextChangedListener(object : DebounceTextWatcher() {
        override fun onDone() {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    if (!this.toString().isNullOrEmpty() && this.toString().isNotEmpty()) {
                        f()
                    }
                } catch (e: java.lang.Exception) {

                }
            }
        }
    })
}

fun View.setVisiblity(visible: Boolean) {
    if (visible) this.visible() else this.gone()
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}


fun hasNetwork(context: Context): Boolean? {
    var isConnected: Boolean? = false // Initial Value
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    if (activeNetwork != null && activeNetwork.isConnected)
        isConnected = true
    return isConnected
}


fun ImageView.glide(context: Context,url: String, roundedCorners: Int = 30) {
    var requestOptions = RequestOptions()
    if (roundedCorners > 0) {
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(roundedCorners))
    }
    Glide.with(context).load(url).apply(requestOptions).into(this)
}

