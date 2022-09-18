package com.radiusagent.assignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.radiusagent.assignment.R
import com.radiusagent.assignment.data.model.OptionsModel
import com.radiusagent.assignment.databinding.ActivityMainBinding
import com.radiusagent.assignment.ui.adapter.FacilitiesAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.ext.realmListOf

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.name
    }

    lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = this

        val facilitiesAdapter = FacilitiesAdapter(object : ItemClickListener<OptionsModel> {
            override fun onClickListener(item: OptionsModel) {
                Toast.makeText(baseContext, "You click ${item.name}", Toast.LENGTH_LONG).show()
            }
        })

        facilitiesAdapter.submitList(mainViewModel.facilities.value ?: realmListOf())
        binding.facilitiesAdapter = facilitiesAdapter

        mainViewModel.facilities.observe(this) {
            Log.d(TAG, "onCreate() == facilities size: ${it?.size}")
            facilitiesAdapter.submitList(it ?: realmListOf())
        }
    }
}