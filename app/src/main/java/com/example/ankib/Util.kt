package com.example.ankib

import android.content.Context
import android.widget.Toast

class Util {
    companion object {
        fun shortToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun longToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}
