package com.example.tuskmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tuskmanager.ui.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SharedViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TaskApplication.component.inject(this)

        initViews()
        initLiveData()
    }

    private fun initViews() {
        val toolbar = tb_top_toolbar
        setSupportActionBar(toolbar)

        tb_top_toolbar.setNavigationOnClickListener {
            viewModel.onBackPressed()
        }
    }

    private fun initLiveData() {
        viewModel.toolBarText.observe(this, Observer {
            tb_top_toolbar.title = it?.displayableName

            when (it ?: return@Observer) {
                SharedViewModel.ToolBarText.ALL_TASKS -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                else -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
            }
        })

        viewModel.screen.observe(this, Observer {
            val fragment = when (it ?: return@Observer) {
                SharedViewModel.Screen.ALL_TASKS -> AllTasksFragment()
                SharedViewModel.Screen.NEW_TASK -> NewTaskFragment()
                SharedViewModel.Screen.ALL_CATEGORIES -> AllCategoriesFragment()
            }

            supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fl_container, fragment)
                .commit()

            when (it) {
                SharedViewModel.Screen.ALL_TASKS -> {
                    fab.show()
                    fab.setImageResource(R.drawable.ic_add)
                }
                else -> {
                    fab.hide()
                   // fab.setImageResource(R.drawable.ic_check)
                }
            }

            viewModel.setToolBarText()
        })

        fab.setOnClickListener {
            viewModel.fabClicked()
        }
    }
}
