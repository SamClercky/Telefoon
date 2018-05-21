package be.samclercky.telefoon.utils

import android.net.Uri

fun String.toUri() = Uri.parse(this)