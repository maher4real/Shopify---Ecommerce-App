package com.maheralivijapura.shopify.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.maheralivijapura.shopify.R
import com.maheralivijapura.shopify.activities.adapters.CartItemsListAdapter
import com.maheralivijapura.shopify.firestore.FirestoreClass
import com.maheralivijapura.shopify.models.CartItem
import com.maheralivijapura.shopify.models.Product
import com.maheralivijapura.shopify.utils.Constants
import kotlinx.android.synthetic.main.activity_cart_list.*

class CartListActivity : BaseActivity() {

    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartListItems: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContentView(R.layout.activity_cart_list)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.app_gradient_color_background)

        setupActionBar()

        btn_checkout.setOnClickListener {
            val intent = Intent(this@CartListActivity, AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS, true)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getProductList()
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_cart_list_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_cart_list_activity.setNavigationOnClickListener { onBackPressed() }
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>) {

        hideProgressDialog()

        for (product in mProductsList) {
            for (cartItem in cartList) {
                if (product.product_id == cartItem.product_id) {

                    cartItem.stock_quantity = product.stock_quantity

                    if (product.stock_quantity.toInt() == 0){
                        cartItem.cart_quantity = product.stock_quantity
                    }
                }
            }
        }
        mCartListItems = cartList

        if (mCartListItems.size > 0) {

            rv_cart_items_list.visibility = View.VISIBLE
            ll_checkout.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE

            rv_cart_items_list.layoutManager = LinearLayoutManager(this@CartListActivity)
            rv_cart_items_list.setHasFixedSize(true)

            val cartListAdapter = CartItemsListAdapter(this@CartListActivity, mCartListItems,true)
            rv_cart_items_list.adapter = cartListAdapter

            var subTotal: Double = 0.0

            for (item in mCartListItems) {

                val availableQuantity = item.stock_quantity.toInt()

                if (availableQuantity > 0) {
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()

                    subTotal += (price * quantity)
                }

            }

            tv_sub_total.text = "₹$subTotal"
            tv_shipping_charge.text = "₹50.0"

            if (subTotal > 0) {
                ll_checkout.visibility = View.VISIBLE

                val total = subTotal + 50
                tv_total_amount.text = "₹$total"
            } else {
                ll_checkout.visibility = View.GONE
            }

        } else {
            rv_cart_items_list.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {
        hideProgressDialog()
        mProductsList = productsList
        getCartItemsList()
    }

    private fun getProductList() {

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAllProductsList(this@CartListActivity)
    }

    private fun getCartItemsList() {

        //showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getCartList(this@CartListActivity)
    }

    fun itemUpdateSuccess() {

        hideProgressDialog()

        getCartItemsList()
    }

    fun itemRemovedSuccess() {

        hideProgressDialog()

        Toast.makeText(
            this@CartListActivity,
            resources.getString(R.string.msg_item_removed_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getCartItemsList()
    }
}