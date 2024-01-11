package com.example.expertsystem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.exceptions.ClientException

class QuestFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.quest_fragment, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val questions = getQuestions()
        questionAdapter = QuestionAdapter(questions)
        val submitButton: Button = view.findViewById(R.id.diagnoseButton)
        submitButton.setOnClickListener {
            val diagnosis = questionAdapter.determineDiagnosis()
            Log.d("Diagnosis", "User Diagnosis: $diagnosis")
        }
        recyclerView.adapter = questionAdapter
        return view
    }

    fun getQuestions(): List<String> {
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
                        session.run("MATCH (q:Question) RETURN q.text AS question")

                    while (result.hasNext()) {
                        val record = result.next()
                        val questionText = record["question"].asString()
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
