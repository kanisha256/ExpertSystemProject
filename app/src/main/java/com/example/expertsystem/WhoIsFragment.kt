package com.example.expertsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.exceptions.ClientException

class WhoIsFragment : Fragment() {

    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var nameTextView: TextView
    private lateinit var biographyTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.who_is_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        autoCompleteTextView = view.findViewById(R.id.autoCompletePerson)
        nameTextView = view.findViewById(R.id.nameTextView)
        biographyTextView = view.findViewById(R.id.biographyTextView)
        val button = view.findViewById<Button>(R.id.button)

        val persons = getPersonSuggestions()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, persons)
        autoCompleteTextView.setAdapter(adapter)

        button.setOnClickListener {
            val enteredPerson = autoCompleteTextView.text.toString()
            queryPersonDetails(enteredPerson)
        }
    }

    private fun queryPersonDetails(person: String) {
        val thread = Thread {
            try {
                val driver = GraphDatabase.driver(
                    "neo4j+ssc://ebce1fa4.databases.neo4j.io:7687",
                    AuthTokens.basic("neo4j", "0hUpEQy1HUGSvMgGtKtlb3rojnWf5axLk-UkzBhDBaY")
                )
                driver.use { driver ->
                    val session = driver.session()
                    val result = session.run("""
                    MATCH (person:Person {fullName: '$person'})
                    RETURN person AS person
                """)

                    if (result.hasNext()) {
                        val record = result.next()
                        if (record.containsKey("person")) {
                            val personValue = record["person"]
                            if (personValue.type().name() == "NODE") {
                                val personNode = personValue.asNode()
                                val personName = personNode["fullName"].asString()
                                val personBiography = personNode["biography"].asString()

                                activity?.runOnUiThread {
                                    nameTextView.text = "ФИО: $personName"
                                    biographyTextView.text = "Биография: $personBiography"
                                }
                            } else {
                                activity?.runOnUiThread {
                                    nameTextView.text = "Такого человека не существует"
                                    biographyTextView.text = ""
                                }
                            }
                        } else {
                            activity?.runOnUiThread {
                                nameTextView.text = "Такого человека не существует"
                                biographyTextView.text = ""
                            }
                        }
                    } else {
                        activity?.runOnUiThread {
                            nameTextView.text = "Такого человека не существует"
                            biographyTextView.text = ""
                        }
                    }
                    session.close()
                }
            } catch (e: ClientException) {
                e.printStackTrace()
            }
        }
        thread.start()
        thread.join()
    }


    private fun getPersonSuggestions(): List<String> {
        val persons = mutableListOf<String>()
        val thread = Thread {
            try {
                val driver = GraphDatabase.driver(
                    "neo4j+ssc://ebce1fa4.databases.neo4j.io:7687",
                    AuthTokens.basic("neo4j", "0hUpEQy1HUGSvMgGtKtlb3rojnWf5axLk-UkzBhDBaY")
                )
                driver.use { driver ->
                    val session = driver.session()
                    val result = session.run("MATCH (person:Person) RETURN person.fullName AS fullName")

                    while (result.hasNext()) {
                        val record = result.next()
                        val personName = record["fullName"].asString()
                        persons.add(personName)
                    }
                    session.close()
                }
            } catch (e: ClientException) {
                e.printStackTrace()
            }
        }
        thread.start()
        thread.join()
        return persons
    }

}
