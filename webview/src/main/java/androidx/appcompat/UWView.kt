package androidx.appcompat

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.webkit.WebView

open class UWView : WebView {
    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    )

    @SuppressLint("NewApi")
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context!!, attrs, defStyleAttr, defStyleRes
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        privateBrowsing: Boolean
    ) : super(
        context!!, attrs, defStyleAttr, privateBrowsing
    )

    override fun reload() {
        super.reload()
        Log.d(TAG, "reload: $url")
    }

    companion object {
        private const val TAG = "@@@"
    }
}
