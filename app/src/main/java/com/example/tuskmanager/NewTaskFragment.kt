package com.example.tuskmanager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_new_task.*
import java.text.SimpleDateFormat
import java.util.*

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
    }


    private fun initViews() {
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
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val inMillis = calendar.timeInMillis
        val pattern = "dd.MM.yyyy"
        val formatter = SimpleDateFormat(pattern)
        val date = formatter.format(Date(inMillis))
        tf_date.editText?.setText(date)
        viewModel.dateSelected(date, inMillis)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        "$hourOfDay:$minute"
        val calendar = Calendar.getInstance()
        calendar.set(1988, 11 , 2, hourOfDay, minute, 0)
        val inMillis = calendar.timeInMillis
        val pattern = "HH:mm"
        val formatter = SimpleDateFormat(pattern)
        val time = formatter.format(Date(inMillis))
        tf_time.editText?.setText(time)
        viewModel.timeSelected(time, inMillis)
    }
}