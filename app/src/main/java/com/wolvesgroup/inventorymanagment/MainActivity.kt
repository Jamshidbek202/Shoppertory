package com.wolvesgroup.inventorymanagment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.wolvesgroup.inventorymanagment.databinding.ActivityMainBinding
import com.wolvesgroup.inventorymanagment.fragments.AlertsFragment
import com.wolvesgroup.inventorymanagment.fragments.DashboardFragment
import com.wolvesgroup.inventorymanagment.fragments.HistoryFragment
import com.wolvesgroup.inventorymanagment.fragments.InventoryManagementFragment
import com.wolvesgroup.inventorymanagment.utils.services.AlertService


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val serviceFragment = Intent(this, AlertService::class.java)
        startService(serviceFragment)

        val pagerAdapter = MyPagerAdapter(supportFragmentManager)
        binding.mainViewPager.adapter = pagerAdapter

        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_dashboard -> binding.mainViewPager.currentItem = 0
                R.id.navigation_inventory_management -> binding.mainViewPager.currentItem = 1
                R.id.navigation_notifications -> binding.mainViewPager.currentItem = 2
                R.id.history_fragment -> binding.mainViewPager.currentItem = 3
            }
            true
        }

        binding.mainViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                binding.bottomNavigation.menu.getItem(position).isChecked = true
            }
        })
    }

    inner class MyPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        override fun getCount(): Int {
            return 4
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> DashboardFragment()
                1 -> InventoryManagementFragment()
                2 -> AlertsFragment()
                3 -> HistoryFragment()
                else -> DashboardFragment()
            }
        }
    }
}