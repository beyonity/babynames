package com.bogarsoft.babynames.utils

import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.util.Locale



fun String.capitalized(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else it.toString()
    }
}



fun EditText.onChange(textChanged: ((String) -> Unit)) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            textChanged.invoke(s.toString())
        }
    })

}

fun Context.showToast(message: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, length).show()
}

fun Fragment.showToast(message: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(requireActivity(), message, length).show()
}

fun View.showSnackMessage(
    message: String?,
    anchorView: View? = null,
    backgroundColor: Int,
    textColor: Int,
    length: Int = Snackbar.LENGTH_SHORT
) {
    message?.let {
        try {
            val snack = Snackbar.make(this, it, length)
            snack.setBackgroundTint(ContextCompat.getColor(context, backgroundColor))
            snack.setTextColor(ContextCompat.getColor(context, textColor))
            snack.anchorView = anchorView
            snack.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}




fun Context.paste():String{
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    if (clipboard.hasPrimaryClip()){
        Log.d("Extension", "paste: "+clipboard.primaryClipDescription)
        if (clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML) == true || clipboard.primaryClipDescription?.hasMimeType(
                ClipDescription.MIMETYPE_TEXT_PLAIN
            ) == true){
            Log.d("Extension", "paste: "+clipboard.primaryClip)
            return clipboard.primaryClip?.getItemAt(0)?.text.toString()

        }else {
            return  ""
        }
    }else {
        return ""
    }
}
fun View.snack(message: String, length: Int= Snackbar.LENGTH_LONG): Snackbar {
    val snack = Snackbar.make(this,message,length)
    snack.show()
    return snack
}

//extention to make string first letter caps without using deprecated methods
fun String.capitalizeWords(): String{
    val words = this.split(" ")
    val capWord = words.map { it.capitalize(Locale.getDefault()) }
    return capWord.joinToString(" ")
}





fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}


