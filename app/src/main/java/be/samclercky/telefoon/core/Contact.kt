package be.samclercky.telefoon.core

import be.samclercky.telefoon.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class Contact(val name: String, val telNumber: String) {
}

class ContactAdaptor(context: Context,
                     val list: ArrayList<Contact>,
                     val resource: Int = R.layout.contact_list_item) : ArrayAdapter<Contact>(
        context, resource, list
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var listview = convertView

        if (listview == null) {
            listview = LayoutInflater.from(context).inflate(resource, parent, false)
        }

        val contact = getItem(position)

        // set propeties
        listview?.findViewById<TextView>(R.id.tv_name)?.text = contact.name
        listview?.findViewById<TextView>(R.id.tv_phoneNo)?.text = contact.telNumber

        if (listview == null) {
            throw IllegalStateException("ContactAdaptor class -> listview is empty")
        } else {
            return listview
        }
    }
}