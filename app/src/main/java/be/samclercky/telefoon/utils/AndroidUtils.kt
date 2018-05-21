package be.samclercky.telefoon.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import be.samclercky.telefoon.R

/**
 * Shows a toast
 * @param message The message to be shown
 * @param duration The length of the toast to be shown, by default LENGTH_SHORT
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * Checks and requests a permission
 * @param permission The persmission to be requested
 * @param showExplanationCallback Callback to say why you want this permission
 * @return true if granted, false if not
 */
fun AppCompatActivity.checkAndRequestPermission(permission: String, requestCode: Int = 1, showExplanationCallback: () -> Unit = {}): Boolean {
    val perm = ContextCompat.checkSelfPermission(this, permission)
    if (perm != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showExplanationCallback()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
        return false
    } else {
        return true
    }
}

fun Context.getStringR(resourceId: Int) = resources.getString(resourceId)
fun Int.getR(context: Context) = context.getStringR(this)
fun EditText.setText(message: String) = this.setText(message, TextView.BufferType.EDITABLE)

fun buildNotification(context: Context,
                      icon: Int = R.mipmap.ic_launcher_round,
                      title: String = "",
                      text: String = "",
                      channelId: String = "be.samclercky.default",
                      priority: Int = NotificationCompat.PRIORITY_DEFAULT)
        = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(priority)