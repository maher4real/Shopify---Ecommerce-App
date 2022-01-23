package com.maheralivijapura.shopify.activities.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.maheralivijapura.shopify.R
import com.maheralivijapura.shopify.activities.CartListActivity
import com.maheralivijapura.shopify.activities.ProductDetailsActivity
import com.maheralivijapura.shopify.activities.SearchActivity
import com.maheralivijapura.shopify.activities.SettingsActivity
import com.maheralivijapura.shopify.activities.adapters.DashboardItemsListAdapter
import com.maheralivijapura.shopify.firestore.FirestoreClass
import com.maheralivijapura.shopify.models.Product
import com.maheralivijapura.shopify.utils.Constants
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_first.*
import java.util.*




class FirstFragment: Fragment(R.layout.fragment_first) {

    private lateinit var mProgressDialog: Dialog

    private val displayDashboardItemsList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onResume() {
        super.onResume()
        getDashboardItemsList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        when (id) {

            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            R.id.action_cart -> {
                startActivity(Intent(activity,CartListActivity::class.java))
                return true
            }
            R.id.action_search -> {
                startActivity(Intent(activity, SearchActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun successDashboardItemsList(dashboardItemsList: ArrayList<Product>) {


        hideProgressDialog()

        if (dashboardItemsList.size > 0) {


            rv_dashboard_items.visibility = View.VISIBLE
            tv_no_dashboard_items_found.visibility = View.GONE

            displayDashboardItemsList.addAll(dashboardItemsList)

            rv_dashboard_items.layoutManager = GridLayoutManager(activity, 2)
            rv_dashboard_items.setHasFixedSize(true)


            val myAdapter = DashboardItemsListAdapter(requireActivity(), displayDashboardItemsList)
            rv_dashboard_items.adapter = myAdapter



            myAdapter.setOnClickListener(object :DashboardItemsListAdapter.OnClickListener{
                override fun onClick(position: Int, product: Product) {
                    val intent = Intent(context,ProductDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID,product.product_id)
                    intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID,product.user_id)
                    startActivity(intent)
                }
            })
        } else {
            rv_dashboard_items.visibility = View.GONE
            tv_no_dashboard_items_found.visibility = View.VISIBLE
        }

    }

    private fun getDashboardItemsList() {

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getDashboardItemsList(this@FirstFragment)
    }

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(requireActivity())

        mProgressDialog.setContentView(R.layout.dialog_progress)

       // mProgressDialog.tv_progress_text.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()
    }
    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }


}