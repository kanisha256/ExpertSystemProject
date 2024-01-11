package com.example.expertsystem.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expertsystem.R

class CurrencyFragment : Fragment() {

    private lateinit var currencyViewModel: CurrencyViewModel
    private lateinit var currencyAdapter: CurrencyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация ViewModel
        currencyViewModel = ViewModelProvider(this).get(CurrencyViewModel::class.java)

        // Инициализация RecyclerView и адаптера
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        currencyAdapter = CurrencyAdapter(emptyList())
        recyclerView.adapter = currencyAdapter

        // Наблюдение за изменениями в списке валют
        currencyViewModel.currencyList.observe(viewLifecycleOwner, Observer { currencies ->
            currencyAdapter.currencyList = currencies
            currencyAdapter.notifyDataSetChanged()
        })
    }
}
