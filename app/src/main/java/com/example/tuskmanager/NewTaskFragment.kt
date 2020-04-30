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
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tuskmanager.ui.TaskAlarmManager
import com.example.tuskmanager.ui.viewmodel.NewTaskViewModelFactory
import kotlinx.android.synthetic.main.fragment_new_task.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class NewTaskFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    @Inject
    lateinit var newTaskViewModelFactory: NewTaskViewModelFactory

    private val newTaskViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            newTaskViewModelFactory
        ).get(NewTaskViewModel::class.java)
    }

    private val args: NewTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        TaskApplication.component.inject(this)
        return inflater.inflate(R.layout.fragment_new_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initLiveData()
    }

    private fun initViews() {
        newTaskViewModel.gotTask(args.clickedTask?.copy())
        fab.setImageResource(R.drawable.ic_check)

        fab.setOnClickListener {
            newTaskViewModel.addTask()
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
                newTaskViewModel.newTitleEntered(it.toString())
            }
        )

        et_task_description.addTextChangedListener(
            afterTextChanged = {
                newTaskViewModel.newDescriptionEntered(it.toString())
            }
        )

        v_choose_category.setOnClickListener {
            findNavController().navigate(R.id.action_newTaskFragment_to_allCategoriesFragment)
        }
    }

    private fun initLiveData() {
        newTaskViewModel.taskCreated.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            val alarmManager = TaskAlarmManager(requireContext())

            val myDateDue = it.dateDue + it.timeDue
            val sdf = SimpleDateFormat("dd.MM.yyyyHH:mm", Locale.ROOT)
            val dateDue = sdf.parse(myDateDue) ?: return@Observer
            alarmManager.startAlarm(
                dateDue.time - TimeUnit.HOURS.toMillis(1),
                it
            )

            findNavController().navigate(R.id.action_newTaskFragment_to_allTasksFragment)
        })

        newTaskViewModel.currentTask.observe(viewLifecycleOwner, Observer {
            Log.d("TAG", it.toString())
            et_task.setText(it.title)
            et_task_description.setText(it.description)
            tf_date.editText?.setText(it.dateDue)
            tf_time.editText?.setText(it.timeDue)
        })

        newTaskViewModel.error.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            Toast.makeText(requireContext(), it, LENGTH_SHORT).show()
        })

        newTaskViewModel.currentCategory.observe(viewLifecycleOwner, Observer {
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
        newTaskViewModel.dateSelected(date)
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
        newTaskViewModel.timeSelected(time)
    }
}