package com.example.expertsystem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuestionAdapter(private val questions: List<String>) :
    RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    private var isQuestion1Yes = false
    private var isQuestion2Yes = false
    private var isQuestion3Yes = false
    private var isQuestion4Yes = false
    private var isQuestion5Yes = false

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionTextView: TextView = itemView.findViewById(R.id.questionTextView)
        val checkBoxYes: CheckBox = itemView.findViewById(R.id.checkBoxYes)
        val checkBoxNo: CheckBox = itemView.findViewById(R.id.checkBoxNo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questions[position]
        holder.questionTextView.text = question

        holder.checkBoxYes.setOnCheckedChangeListener { _, isChecked ->
            updateVariables(position, isChecked)
        }

        holder.checkBoxNo.setOnCheckedChangeListener { _, isChecked ->
            updateVariables(position, !isChecked)
        }
    }

    private fun updateVariables(position: Int, isChecked: Boolean) {
        when (position) {
            0 -> isQuestion1Yes = isChecked
            1 -> isQuestion2Yes = isChecked
            2 -> isQuestion3Yes = isChecked
            3 -> isQuestion4Yes = isChecked
            4 -> isQuestion5Yes = isChecked
        }
    }

    fun determineDiagnosis(): String {
        return when {
            isQuestion1Yes && isQuestion3Yes && isQuestion5Yes -> "Diagnosis A"
            isQuestion2Yes && isQuestion4Yes -> "Diagnosis B"
            isQuestion1Yes && isQuestion2Yes && isQuestion3Yes -> "Diagnosis C"
            else -> "No specific diagnosis"
        }
    }

    override fun getItemCount(): Int {
        return questions.size
    }
}

