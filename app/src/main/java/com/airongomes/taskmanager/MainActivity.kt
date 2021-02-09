package com.airongomes.taskmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.*
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.airongomes.taskmanager.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), DrawerLockMode {

    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
                R.layout.activity_main)
        drawerLayout = binding.drawerLayout

        val navController = this.findNavController(R.id.nav_host_fragment)
        setSupportActionBar(binding.toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

    }

    /**
     * Support Navigation Up
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    /**
     * Define if drawer swiping gesture is locked or unlocked
     */
    override fun setDrawerLocked(locked: Boolean) {
        drawerLayout.setDrawerLockMode(
                if (locked) {
                    LOCK_MODE_LOCKED_CLOSED
                }
                else{
                    LOCK_MODE_UNLOCKED
                })
    }
}