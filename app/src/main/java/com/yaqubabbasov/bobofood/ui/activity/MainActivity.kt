package com.yaqubabbasov.bobofood.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navhostfragment= supportFragmentManager.findFragmentById(R.id.navhostfr) as NavHostFragment
        val nav= navhostfragment.navController
        NavigationUI.setupWithNavController(binding.bottomnavigation,nav)
        nav.addOnDestinationChangedListener { _,destination, _ ->
            when(destination.id){
                R.id.loginFragment, R.id.detailFragment, R.id.splashFragment, R.id.signUpfragment->binding.bottomnavigation.visibility= View.GONE
                R.id.homeFragment, R.id.cartFragment->binding.bottomnavigation.visibility= View.VISIBLE
            }
        }
        binding.bottomnavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeFragment -> {

                    nav.navigate(R.id.homeFragment)
                    true
                }
                R.id.cartFragment -> {
                    nav.navigate(R.id.cartFragment)
                    true
                }
                R.id.favouriteFragment-> {
                    nav.navigate(R.id.favouriteFragment)
                    true
                }
                R.id.profilFragment-> {
                    nav.navigate(R.id.profilFragment)
                    true
                }

                else -> false
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomnavigation) { v, insets ->
            v.setPadding(0, 0, 0, 0)
            insets
        }




    }





}