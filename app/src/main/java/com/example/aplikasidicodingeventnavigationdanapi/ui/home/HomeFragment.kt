package com.example.aplikasidicodingeventnavigationdanapi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasidicodingeventnavigationdanapi.EventAdapter
import com.example.aplikasidicodingeventnavigationdanapi.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var upcomingEventAdapter: EventAdapter
    private lateinit var finishedEventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upcomingEventAdapter = EventAdapter()
        finishedEventAdapter = EventAdapter()

        binding.upcomingEventRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = upcomingEventAdapter
        }

        binding.finishedEventRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = finishedEventAdapter
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        homeViewModel.listEventsItemUpcoming.observe(viewLifecycleOwner) { events ->
            if (events != null) {
                upcomingEventAdapter.submitList(events)
            } else {
                Toast.makeText(context, "No upcoming events found", Toast.LENGTH_SHORT).show()
            }
        }

        homeViewModel.listEventsItemFinished.observe(viewLifecycleOwner) { events ->
            if (events != null) {
                finishedEventAdapter.submitList(events)
            } else {
                Toast.makeText(context, "No finished events found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}