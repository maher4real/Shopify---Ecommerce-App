package com.maheralivijapura.shopify.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.maheralivijapura.shopify.R
import com.maheralivijapura.shopify.firestore.FirestoreClass
import com.maheralivijapura.shopify.models.CartItem
import com.maheralivijapura.shopify.models.Product
import com.maheralivijapura.shopify.utils.Constants
import com.maheralivijapura.shopify.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mProductId: String = ""
    private lateinit var mProductDetails: Product
    private var mProductOwnerId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContentView(R.layout.activity_product_details)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.app_gradient_color_background)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            mProductOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        if (FirestoreClass().getCurrentUserID() == mProductOwnerId) {
            btn_add_to_cart.visibility = View.GONE
            btn_go_to_cart.visibility = View.GONE
        } else {
            btn_add_to_cart.visibility = View.VISIBLE
        }

        btn_add_to_cart.setOnClickListener(this)
        btn_go_to_cart.setOnClickListener(this)

        getProductDetails()
    }

    private fun getProductDetails() {

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductDetails(this@ProductDetailsActivity, mProductId)
    }

    fun productExistsInCart() {

        hideProgressDialog()

        btn_add_to_cart.visibility = View.GONE
        btn_go_to_cart.visibility = View.VISIBLE
    }

    fun productDetailsSuccess(product: Product) {

        mProductDetails = product
        //hideProgressDialog()
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            iv_product_detail_image
        )

        tv_product_details_title.text = product.title
        tv_product_details_price.text = "₹${product.price}"
        tv_product_details_description.text = product.description
        tv_product_details_available_quantity.text = product.stock_quantity

        if(product.stock_quantity.toInt() == 0){

            hideProgressDialog()

            btn_add_to_cart.visibility = View.GONE

            tv_product_details_available_quantity.text =
                resources.getString(R.string.lbl_out_of_stock)

            tv_product_details_available_quantity.setTextColor(
                ContextCompat.getColor(
                    this@ProductDetailsActivity,
                    R.color.colorSnackBarError
                )
            )
        }else{
            if (FirestoreClass().getCurrentUserID() == product.user_id) {
                hideProgressDialog()
            } else {
                FirestoreClass().checkIfItemExistInCart(this@ProductDetailsActivity, mProductId)
            }
        }

    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_product_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun addToCart() {

        val cartItem = CartItem(
            FirestoreClass().getCurrentUserID(),
            mProductOwnerId,

            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addCartItems(this@ProductDetailsActivity, cartItem)
    }


    fun addToCartSuccess() {

        hideProgressDialog()

        Toast.makeText(
            this@ProductDetailsActivity,
            resources.getString(R.string.success_message_item_added_to_cart),
            Toast.LENGTH_SHORT
        ).show()

        btn_add_to_cart.visibility = View.GONE
        btn_go_to_cart.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {

                R.id.btn_add_to_cart -> {
                    addToCart()
                }
                R.id.btn_go_to_cart->{
                    startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
                }
            }
        }
    }
}