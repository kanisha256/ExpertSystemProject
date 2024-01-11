package com.example.expertsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.math.pow

class InterestCalculatorFragment : Fragment() {

    private lateinit var principalEditText: EditText
    private lateinit var rateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var calculateButton: Button
    private lateinit var resultTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_interest_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        principalEditText = view.findViewById(R.id.principalEditText)
        rateEditText = view.findViewById(R.id.rateEditText)
        timeEditText = view.findViewById(R.id.timeEditText)
        calculateButton = view.findViewById(R.id.calculateButton)
        resultTextView = view.findViewById(R.id.resultTextView)

        calculateButton.setOnClickListener {
            calculateInterest()
        }
    }

    private fun calculateInterest() {
        val principal = principalEditText.text.toString().toDoubleOrNull()
        val rate = rateEditText.text.toString().toDoubleOrNull()
        val time = timeEditText.text.toString().toDoubleOrNull()

        val compoundingFrequency = 12

        if (principal != null && rate != null && time != null) {
            val compoundInterest = principal * (1 + rate / (compoundingFrequency * 100)).pow(compoundingFrequency * time)
            val result = compoundInterest - principal
            resultTextView.text = result.toString()
        } else {
            resultTextView.text = "Неправильно"
        }
    }

}
