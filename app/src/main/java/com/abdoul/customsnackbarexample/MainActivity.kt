package com.abdoul.customsnackbarexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.abdoul.customsnackbar.CustomSnackBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        snackBarFab.setOnClickListener {
            showCustomSnackBar(it)
        }
    }

    private val actionClickListener: View.OnClickListener = View.OnClickListener {
        Toast.makeText(this, "Location Has been removed", Toast.LENGTH_SHORT).show()
    }

    private fun showCustomSnackBar(view: View) {
        val customSnackBar = CustomSnackBar.make(
            view = view,
            message = "Location successfully saved",
            icon = R.drawable.ic_location,
            backgroundColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        )
        /*
        * if you just want to show the snackBar call
        *  customSnackBar?.show()
        * */

        //set Action
        customSnackBar?.action(actionLabel = "UNDO", listener = actionClickListener)

        //add Action CallBack
        customSnackBar?.addCallback(object : CustomSnackBar.Callback() {
            override fun onShown(sb: CustomSnackBar) {
                showToast("Custom snackBar showing")
            }
            override fun onDismissed(transientBottomBar: CustomSnackBar, event: Int) {
                when (event) {
                    DISMISS_EVENT_ACTION -> {
                        showToast("User dismissed")
                    }
                    DISMISS_EVENT_TIMEOUT -> {
                        showToast("Time out")
                    }
                    DISMISS_EVENT_CONSECUTIVE -> {
                        showToast("Consecutive dismiss")
                    }
                    DISMISS_EVENT_SWIPE -> {
                        showToast("Swipe dismiss")
                    }

                    DISMISS_EVENT_MANUAL -> {
                        showToast("Manual dismiss")
                    }
                }
            }
        })
        customSnackBar?.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
