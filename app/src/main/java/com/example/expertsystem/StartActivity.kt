package com.example.expertsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.expertsystem.currency.CurrencyFragment
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.exceptions.ClientException

class StartActivity : Fragment() {

    private lateinit var autoCompleteTextView: AutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.start_fragment, container, false)

        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView)
        val button = view.findViewById<Button>(R.id.button)

        button.setOnClickListener {
            val enteredText = autoCompleteTextView.text.toString()

            val keywords = getKeyWords().toTypedArray()

            val foundKeyword = keywords.find { enteredText.contains(it) }

            if (foundKeyword != null) {
                when (foundKeyword) {
                    "Посчитай" -> {
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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up AutoCompleteTextView with suggestions
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, getKeyWords())
        autoCompleteTextView.setAdapter(adapter)
    }

    fun getKeyWords(): List<String> {
        val questions = mutableListOf<String>()
        val thread = Thread {
            try {
                val driver = GraphDatabase.driver(
                    "neo4j+ssc://ebce1fa4.databases.neo4j.io:7687",
                    AuthTokens.basic("neo4j", "0hUpEQy1HUGSvMgGtKtlb3rojnWf5axLk-UkzBhDBaY")
                )
                driver.use { driver ->
                    val session = driver.session()
                    val result =
                        session.run("MATCH (k:Keywords) RETURN k.text AS keywords")

                    while (result.hasNext()) {
                        val record = result.next()
                        val questionText = record["keywords"].asString()
                        questions.add(questionText)
                    }
                    session.close()
                }
            } catch (e: ClientException) {
                e.printStackTrace()
            }
        }
        thread.start()
        thread.join()
        return questions
    }
}
