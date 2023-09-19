package com.example.climaxtest.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.climaxtest.R
import com.example.climaxtest.activities.SelectImageActivity
import com.example.climaxtest.activities.TaskForCardView
import com.example.climaxtest.adapters.DataAdapter
import com.example.climaxtest.repository.PersonDataRepository
import com.example.climaxtest.room.RoomDataBase
import com.example.climaxtest.viewmodels.MainViewModel
import com.example.climaxtest.viewmodels.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {
    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val quotesDao = RoomDataBase.getDataBase(requireContext()).personDao()
        val repository = PersonDataRepository(quotesDao)
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val repository =NoteRepository(note)

        recylerView.setHasFixedSize(true)
        recylerView.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        mainViewModel.getDao().observe(viewLifecycleOwner, Observer {
            recylerView.adapter = DataAdapter(it)
        })



        openTask1.setOnClickListener {
            val intent = Intent(requireContext(), SelectImageActivity::class.java)
            startActivity(intent)
        }

        openTask2.setOnClickListener {
            val intent = Intent(requireContext(), TaskForCardView::class.java)
            startActivity(intent)
        }
        addButton.setOnClickListener {

             val action = HomeFragmentDirections.actionHomeFragmentToAddDataFragment()
             findNavController().navigate(action)
        }
    }
}