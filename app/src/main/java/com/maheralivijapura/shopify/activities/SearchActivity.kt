package com.maheralivijapura.shopify.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.maheralivijapura.shopify.R
import com.maheralivijapura.shopify.activities.adapters.DashboardItemsListAdapter
import com.maheralivijapura.shopify.firestore.FirestoreClass
import com.maheralivijapura.shopify.models.Product
import com.maheralivijapura.shopify.utils.Constants
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_first.*
import java.util.*
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog


    private val displayDashboardItemsList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContentView(R.layout.activity_search)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.app_gradient_color_background)

        search_list_activity.setOnClickListener {
            val intent = Intent(this@SearchActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun successSearchItemsList(dashboardItemsList: ArrayList<Product>) {

        hideProgressDialog()

        if (dashboardItemsList.size > 0)
        {

            rv_search_items.visibility = View.VISIBLE
            tv_no_search_items_found.visibility = View.GONE

            displayDashboardItemsList.addAll(dashboardItemsList)

            rv_search_items.layoutManager = GridLayoutManager(this, 2)
            rv_search_items.setHasFixedSize(true)


            val myAdapter = DashboardItemsListAdapter(this, displayDashboardItemsList)
            rv_search_items.adapter = myAdapter


            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText!!.isNotEmpty()) {
                        displayDashboardItemsList.clear()
                        val search = newText.lowercase(Locale.getDefault())
                        dashboardItemsList.forEach {
                            if (it.title.lowercase(Locale.getDefault()).contains(search)) {
                                displayDashboardItemsList.add(it)
                            }
                        }
                        rv_search_items.adapter!!.notifyDataSetChanged()
                    } else {
                        displayDashboardItemsList.clear()
                        displayDashboardItemsList.addAll(dashboardItemsList)
                        rv_search_items.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }
            })


            myAdapter.setOnClickListener(object : DashboardItemsListAdapter.OnClickListener{
                override fun onClick(position: Int, product: Product) {
                    val intent = Intent(this@SearchActivity,ProductDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID,product.product_id)
                    intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID,product.user_id)
                    startActivity(intent)
                }
            })

        }
        else
        {
            rv_search_items.visibility = View.GONE
            tv_no_search_items_found.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        getSearchItemsList()
    }


    private fun getSearchItemsList(){

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getSearchItemsList(this@SearchActivity)
    }

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)

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