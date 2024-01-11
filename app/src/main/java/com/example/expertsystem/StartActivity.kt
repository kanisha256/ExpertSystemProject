package com.example.expertsystem


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.expertsystem.currency.CurrencyFragment

class StartActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.start_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editText = view.findViewById<EditText>(R.id.editText)
        val button = view.findViewById<Button>(R.id.button)

        button.setOnClickListener {
            val enteredText = editText.text.toString()

            val keywords = arrayOf(
                "Диагноз", "Посчитай", "Психология", "Вычисли", "Болезнь","class","Кто это","Валюта","Калькулятор процента по вкладу"
            )

            val foundKeyword = keywords.find { enteredText.contains(it) }

            if (foundKeyword != null) {
                when (foundKeyword) {
                    "Посчитай"-> {
                        val mathExpression = enteredText.replace("Посчитай", "").trim()
                        val mathFragment = MathFragment()

                        val args = Bundle()
                        args.putString("mathExpression", mathExpression)
                        mathFragment.arguments = args

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, mathFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    "Кто это" -> {
                        val whoIsFragment = WhoIsFragment()

                        val args = Bundle()
                        args.putString("query", enteredText.replace("кто это", "").trim())
                        whoIsFragment.arguments = args

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, whoIsFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    "Диагноз" -> {
                        val questFragment = QuestFragment()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, questFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    "Валюта" -> {
                        val currencyFragment = CurrencyFragment()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, currencyFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    "Калькулятор процента по вкладу" -> {
                        val calculatorFragment = InterestCalculatorFragment()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, calculatorFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Тема не найдена", Toast.LENGTH_LONG).show()
            }
        }
    }

}