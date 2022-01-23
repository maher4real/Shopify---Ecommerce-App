package com.maheralivijapura.shopify.activities.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.maheralivijapura.shopify.R
import com.maheralivijapura.shopify.activities.adapters.SoldProductsListAdapter
import com.maheralivijapura.shopify.firestore.FirestoreClass
import com.maheralivijapura.shopify.models.SoldProduct
import kotlinx.android.synthetic.main.fragment_fourth.*

class FourthFragment : Fragment(R.layout.fragment_fourth) {

    private lateinit var mProgressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fourth, container, false)
    }

    override fun onResume() {
        super.onResume()
        getSoldProductsList()
    }

    private fun getSoldProductsList() {

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getSoldProductsList(this@FourthFragment)
    }

    fun successSoldProductsList(soldProductsList: ArrayList<SoldProduct>) {

        hideProgressDialog()

        if (soldProductsList.size > 0) {
            rv_sold_product_items.visibility = View.VISIBLE
            tv_no_sold_products_found.visibility = View.GONE

            rv_sold_product_items.layoutManager = LinearLayoutManager(activity)
            rv_sold_product_items.setHasFixedSize(true)

            val soldProductsListAdapter =
                SoldProductsListAdapter(requireActivity(), soldProductsList)
            rv_sold_product_items.adapter = soldProductsListAdapter
        } else {
            rv_sold_product_items.visibility = View.GONE
            tv_no_sold_products_found.visibility = View.VISIBLE
        }
    }

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(requireActivity())

        mProgressDialog.setContentView(R.layout.dialog_progress)

        //mProgressDialog.tv_progress_text.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()
    }
    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

}