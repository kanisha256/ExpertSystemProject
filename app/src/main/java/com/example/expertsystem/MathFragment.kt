package com.example.expertsystem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import net.objecthunter.exp4j.ExpressionBuilder

class MathFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_math, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resultTextView = view.findViewById<TextView>(R.id.resultTextView)

        val mathExpression = arguments?.getString("mathExpression")

        Log.d("MathFragment", "Received math expression: $mathExpression")

        try {
            val result = ExpressionBuilder(mathExpression).build().evaluate()
            Log.d("MathFragment", "Result: $result")
            resultTextView.text = "Результат: $result"
        } catch (e: Exception) {
            Log.e("MathFragment", "Error during expression evaluation", e)
            resultTextView.text = "Ошибка при вычислении математического выражения"
        }
    }

}
