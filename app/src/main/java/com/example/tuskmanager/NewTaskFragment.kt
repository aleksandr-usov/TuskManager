package com.example.tuskmanager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_new_task.*
import java.util.*
import kotlin.math.min

class NewTaskFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLiveData()
        initViews()

        // val navController = Navigation.findNavController(view)

        v_choose_category.setOnClickListener {
            findNavController().navigate(R.id.action_newTaskFragment_to_allCategoriesFragment)
        }
    }

    private fun initViews() {
        fab.setImageResource(R.drawable.ic_check)

        v_choose_category.setOnClickListener {
            viewModel.chooseCategoryClicked()
        }

        tf_date.setStartIconOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_newTaskFragment_to_allTasksFragment)
            viewModel.addTask()
        }

        tf_time.setStartIconOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show()
        }

        et_task.addTextChangedListener(
            afterTextChanged = {
                viewModel.newTitleEntered(it.toString())
            })

        et_task_description.addTextChangedListener(
            afterTextChanged = {
                viewModel.newDescriptionEntered(it.toString())
            })
    }

    private fun initLiveData() {
        viewModel.currentCategory.observe(viewLifecycleOwner, Observer {
            iv_choose_category.setImageResource(
                resources.getIdentifier(
                    it.icon,
                    "drawable",
                    "com.example.tuskmanager"
                )
            )
            iv_choose_category.setColorFilter(Color.parseColor(it.color))
            tv_choose_category.text = it.title
            tv_choose_category.setTextColor(Color.parseColor(it.color))
        })

        viewModel.currentTask.observe(viewLifecycleOwner, Observer {
            tf_task.editText?.setText(it.title)
            tf_task_description.editText?.setText(it.description)
            tf_date.editText?.setText(it.dateDue)
            tf_time.editText?.setText(it.timeDue)
        })
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
       val dayToDisplay = if (dayOfMonth < 10) {
           "0$dayOfMonth"
       } else dayOfMonth

        val monthToDisplay = if (month + 1 < 10) {
            "0${month + 1}"
        } else month + 1

        val date = "$dayToDisplay.$monthToDisplay.$year"
        tf_date.editText?.setText(date)
        viewModel.dateSelected(date)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val hourToDisplay = if (hourOfDay < 10) {
            "0$hourOfDay"
        } else hourOfDay

        val minuteToDisplay = if (minute < 10) {
            "0$minute"
        } else minute

        val time = "$hourToDisplay:$minuteToDisplay"
       tf_time.editText?.setText(time)
        viewModel.timeSelected(time)
    }
}