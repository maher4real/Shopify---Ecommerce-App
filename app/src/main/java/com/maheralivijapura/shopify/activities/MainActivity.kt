package com.maheralivijapura.shopify.activities

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maheralivijapura.shopify.R
import com.maheralivijapura.shopify.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.app_gradient_color_background)

        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.app_gradient_color_background
            )
        )

        val sharedPreferences =
            getSharedPreferences(Constants.SHOPIFY_PREFERENCES, Context.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!

        //val navView: BottomNavigationView = findViewById(R.id.nav_view)

//        val firstFragment= FirstFragment()
//        val secondFragment= SecondFragment()
//        val thirdFragment= ThirdFragment()
//        val fourthFragment= FourthFragment()
//
//        //setCurrentFragment(firstFragment)
//
//        navView.setOnItemSelectedListener {
//            when(it.itemId){
//                R.id.navigation_dashboard->setCurrentFragment(firstFragment)
//                R.id.navigation_products->setCurrentFragment(secondFragment)
//                R.id.navigation_orders->setCurrentFragment(thirdFragment)
//                R.id.navigation_sold_products->setCurrentFragment(fourthFragment)
//            }
//            true
//        }

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_products,
                R.id.navigation_orders,
                R.id.navigation_sold_products
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)



    }

//    private fun setCurrentFragment(fragment: Fragment)=
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.nav_host_fragment,fragment)
//            commit()
//    }

    override fun onBackPressed() {
        doubleBackToExit()
    }
}