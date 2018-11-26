package com.codingchili.mouse.enigma.presenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.codingchili.mouse.enigma.R
import com.codingchili.mouse.enigma.model.Credential
import com.codingchili.mouse.enigma.model.CredentialBank
import com.codingchili.mouse.enigma.model.FaviconLoader
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CredentialListFragment : Fragment() {
    private lateinit var adapter: ArrayAdapter<Credential>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Toolbar>(R.id.bottom_app_bar).setOnClickListener {
            val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
            bottomNavDrawerFragment.show(activity?.supportFragmentManager, bottomNavDrawerFragment.tag)
        }

        val button = view.findViewById<FloatingActionButton>(R.id.add_pw)

        button.setOnClickListener {
            view.findViewById<FloatingActionButton>(R.id.add_pw).setImageResource(R.drawable.add_icon_simple)
            FragmentSelector.addCredential()
        }

        val list = view.findViewById<ListView>(R.id.list_pw)
        adapter = object : ArrayAdapter<Credential>(activity?.applicationContext!!, R.layout.list_item_user, CredentialBank.retrieve()) {

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
                var view: View? = convertView
                if (convertView == null) {
                    view = layoutInflater.inflate(R.layout.list_item_user, parent, false) as View
                }

                val imageView: ImageView = view?.findViewById(R.id.site_logo) as ImageView

                FaviconLoader(context).get(CredentialBank.retrieve()[position].site, { bitmap ->
                    imageView.setImageBitmap(bitmap)
                }, {
                    // image not loaded to cache - unload current.
                    imageView.setImageDrawable(null)
                })

                view.findViewById<TextView>(R.id.url)?.text = CredentialBank.retrieve()[position].site
                view.findViewById<TextView>(R.id.username)?.text = CredentialBank.retrieve()[position].username

                return view
            }
        }

        CredentialBank.onChangeListener {
            adapter.notifyDataSetChanged()
        }

        list?.adapter = adapter

        list?.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            FragmentSelector.credentialInfo(CredentialBank.retrieve()[position])
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        return inflater.inflate(R.layout.fragment_user_list, container,false)
    }

}