package be.samclercky.telefoon.core

import android.Manifest
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import be.samclercky.telefoon.R
import be.samclercky.telefoon.utils.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class TelefoonActions(val context: Context) {

    companion object {
        val contacts = arrayListOf<Contact>()
        private var isStartedPopulating = false
    }

    fun populateContacts(onCompletion: () -> Unit = {}) {
        if (!isStartedPopulating) {
            // start populating contacts
            launch(UI) {
                getAndAddContacts(this@TelefoonActions.context)
                onCompletion()
            }
        }
    }

    private suspend fun getAndAddContacts(context: Context) = withContext(CommonPool) {
        // notes every other instance that it is already buzy collecting
        withContext(UI) { isStartedPopulating = true}

        val phones = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        while (phones.moveToNext()) {
            val name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val contact = Contact(name, phoneNumber)
            withContext(UI) { contacts.add(contact)}
        }
        phones.close()
    }

    fun call(phoneNo: String) {
        if (!phoneNo.isEmpty()) {
            val dial = "tel:$phoneNo"
            startActivity(context, Intent(Intent.ACTION_CALL, dial.toUri()), null)
        } else {
            context.showToast(R.string.error_no_phoneNo.getR(context))
        }
    }
}