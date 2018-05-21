package be.samclercky.telefoon.ui

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import be.samclercky.telefoon.R
import be.samclercky.telefoon.core.ContactAdaptor
import be.samclercky.telefoon.core.TelefoonActions
import be.samclercky.telefoon.utils.checkAndRequestPermission
import be.samclercky.telefoon.utils.getR
import be.samclercky.telefoon.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {
    val MY_PERMISSION_REQUEST_ACTION_CALL = 1
    val MY_PERMISSION_REQUEST_READ_CONTACTS = 2
    private val tActions = TelefoonActions(this)
    private var prevNo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        populateContactsAndCheck()
        getDialPhoneNoAndSet()

        val mPhoneNoEt = et_tel_no
        btn_call.setOnClickListener {
            val phoneNo  = mPhoneNoEt.text.toString()
            callAndCheck(phoneNo)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSION_REQUEST_ACTION_CALL -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callAndCheck(prevNo)
                }
            }
            MY_PERMISSION_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    populateContactsAndCheck()
                }
            }
        }
    }

    private fun callAndCheck(phoneNo: String): Boolean {
        if (checkAndRequestPermission(Manifest.permission.CALL_PHONE)) {
            tActions.call(phoneNo)
            return true
        } else {
            prevNo = phoneNo
            showToast(R.string.error_no_permission.getR(this))
            return false
        }
    }
    private fun populateContactsAndCheck() {
        if (checkAndRequestPermission(Manifest.permission.READ_CONTACTS, MY_PERMISSION_REQUEST_READ_CONTACTS)) {
            tActions.populateContacts({addListAdapter()})
        } else {
            showToast(R.string.error_no_permission.getR(this))
        }
    }
    private fun getDialPhoneNoAndSet() {
        val intent = this.intent
        et_tel_no.setText(intent.data?.toString()?.substring(4) ?: "")
    }

    private fun addListAdapter() {
        val contactAdaptor = ContactAdaptor(this, TelefoonActions.contacts)
        lv_contacts.adapter = contactAdaptor
    }
}
