package com.example.tuskmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tuskmanager.ui.viewmodel.AllTasksViewModelFactory
import com.example.tuskmanager.ui.viewmodel.NewTaskViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var newTaskViewModelFactory: NewTaskViewModelFactory

    @Inject
    lateinit var allTasksViewModelFactory: AllTasksViewModelFactory

    private val newTaskViewModel by lazy {
        ViewModelProvider(this, newTaskViewModelFactory).get(NewTaskViewModel::class.java)
    }

    private val allTasksViewModel by lazy {
        ViewModelProvider(this, allTasksViewModelFactory).get(AllTasksViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TaskApplication.component.inject(this)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawer)

        setSupportActionBar(tb_top_toolbar)

        tb_top_toolbar.setupWithNavController(navController, appBarConfiguration)

        setupActionBarWithNavController(navController, appBarConfiguration)

        nv_drawer.setupWithNavController(navController)

        val submenu = nv_drawer.menu.addSubMenu("Search by category")
        newTaskViewModel.allCategories.observe(this, Observer {
            it.forEach { menuItem ->
                val item = submenu.add(menuItem.title)
                item.isCheckable = true

                item.setIcon(
                    resources.getIdentifier(
                        menuItem.icon,
                        "drawable",
                        "com.example.tuskmanager"
                    )
                )
            }
        })
        nv_drawer.setNavigationItemSelectedListener {
            allTasksViewModel.sortByCategory(it.title.toString())
            drawer.closeDrawer(nv_drawer)
            true
        }
    }
}
