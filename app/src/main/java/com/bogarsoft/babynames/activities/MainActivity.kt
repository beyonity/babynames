package com.bogarsoft.babynames.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bogarsoft.babynames.R
import com.bogarsoft.babynames.adapters.PageAdapter
import com.bogarsoft.babynames.databinding.ActivityMainBinding
import com.bogarsoft.babynames.fragments.HomeFragment
import com.bogarsoft.babynames.fragments.PageOneFragment
import com.bogarsoft.babynames.fragments.PageTwoFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var pageAdapter: PageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }
    private fun init(){
        pageAdapter= PageAdapter(supportFragmentManager)
        pageAdapter.addFragment(HomeFragment(),"Home")
        pageAdapter.addFragment(PageOneFragment(),"Page One")
        pageAdapter.addFragment(PageTwoFragment(),"Page Two")
        binding.viewpager.offscreenPageLimit=5
        binding.viewpager.adapter=pageAdapter
        binding.viewpager.disableScroll(true)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home ->{
                    binding.viewpager.setCurrentItem(0,false)
                    true
                }
                R.id.pageone ->{
                    binding.viewpager.setCurrentItem(1,false)
                    true
                }

                R.id.pagetwo -> {
                    binding.viewpager.setCurrentItem(2, false)
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
}