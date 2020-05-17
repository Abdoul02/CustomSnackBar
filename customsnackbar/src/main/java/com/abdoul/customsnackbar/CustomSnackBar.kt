package com.abdoul.customsnackbar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.abdoul.customsnackbar.CustomSnackBar.Callback.Companion.DISMISS_EVENT_ACTION
import com.google.android.material.snackbar.BaseTransientBottomBar

class CustomSnackBar(
    parent: ViewGroup,
    content: CustomSnackBarView
) : BaseTransientBottomBar<CustomSnackBar>(parent, content, content) {

    init {
        getView().setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                android.R.color.transparent
            )
        )
        getView().setPadding(0, 0, 0, 0)
    }

    companion object {
        lateinit var customView: CustomSnackBarView
        fun make(
            view: View,
            message: String, icon: Int, backgroundColor: Int
        ): CustomSnackBar? {

            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            try {
                customView = LayoutInflater.from(view.context).inflate(
                    R.layout.snack_bar,
                    parent,
                    false
                ) as CustomSnackBarView

                customView.tvMsg.text = message
                customView.imLeft.setImageResource(icon)
                customView.layRoot.setBackgroundColor(backgroundColor)

                return CustomSnackBar(parent, customView)
            } catch (e: Exception) {
                Log.v("exception ", e.message)
            }

            return null
        }

        private fun View?.findSuitableParent(): ViewGroup? {
            var view = this
            var fallback: ViewGroup? = null
            do {
                if (view is CoordinatorLayout) {
                    return view
                } else if (view is FrameLayout) {
                    if (view.id == android.R.id.content) {
                        return view
                    } else {
                        fallback = view
                    }
                }

                if (view != null) {
                    val parent = view.parent
                    view = if (parent is View) parent else null
                }
            } while (view != null)

            return fallback
        }
    }

    fun action(actionLabel: String, listener: View.OnClickListener?): CustomSnackBar {
        val textViewAction = customView.tvAction
        textViewAction.text = actionLabel
        textViewAction.setOnClickListener {
            listener?.onClick(textViewAction)
            dispatchActionDismissEvent()
        }
        return this
    }

    private fun dispatchActionDismissEvent() {
        dispatchDismiss(DISMISS_EVENT_ACTION)
    }

    open class Callback : BaseCallback<CustomSnackBar>() {
        override fun onShown(sb: CustomSnackBar) {}

        override fun onDismissed(transientBottomBar: CustomSnackBar, @DismissEvent event: Int) {
        }

        companion object {
            /** Indicates that the Snackbar was dismissed via a swipe.  */
            const val DISMISS_EVENT_SWIPE = BaseCallback.DISMISS_EVENT_SWIPE

            /** Indicates that the Snackbar was dismissed via an action click.  */
            const val DISMISS_EVENT_ACTION = BaseCallback.DISMISS_EVENT_ACTION

            /** Indicates that the Snackbar was dismissed via a timeout.  */
            const val DISMISS_EVENT_TIMEOUT = BaseCallback.DISMISS_EVENT_TIMEOUT

            /** Indicates that the Snackbar was dismissed via a call to [.dismiss].  */
            const val DISMISS_EVENT_MANUAL = BaseCallback.DISMISS_EVENT_MANUAL

            /** Indicates that the Snackbar was dismissed from a new Snackbar being shown.  */
            const val DISMISS_EVENT_CONSECUTIVE = BaseCallback.DISMISS_EVENT_CONSECUTIVE
        }
    }
}