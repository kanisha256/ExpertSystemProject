package com.example.expertsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.types.Node
import org.neo4j.driver.exceptions.ClientException

class WhoIsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.who_is_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val thread = Thread {
            try {
                val driver = GraphDatabase.driver(
                    "neo4j+ssc://ebce1fa4.databases.neo4j.io:7687",
                    AuthTokens.basic("neo4j", "0hUpEQy1HUGSvMgGtKtlb3rojnWf5axLk-UkzBhDBaY")
                )
                driver.use { driver ->
                    val session = driver.session()
                    val result = session.run("""
                    MATCH (person:Person)
                    RETURN person.fullName AS fullName, person.biography AS biography
                """)
                    val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
                    val biographyTextView = view.findViewById<TextView>(R.id.biographyTextView)

                    if (result.hasNext()) {
                        val record = result.next()
                        if (record.containsKey("person")) {
                            val personNode = record["person"] as Node
                            val personName = personNode["fullName"].asString()
                            val personBiography = personNode["biography"].asString()

                            nameTextView.text = "ФИО: $personName"
                            biographyTextView.text = "Биография: $personBiography"
                        } else {
                            nameTextView.text = "Такого человека не существует"
                            biographyTextView.text = ""
                        }
                    } else {
                        nameTextView.text = "Такого человека не существует"
                        biographyTextView.text = ""
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

}