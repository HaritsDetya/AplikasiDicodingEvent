package com.example.aplikasidicodingeventnavigationdanapi.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasidicodingeventnavigationdanapi.EventAdapter
import com.example.aplikasidicodingeventnavigationdanapi.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UpcomingViewModel by viewModels()

    private lateinit var upcomingEventAdapter: EventAdapter

    companion object {
        const val EXTRA_QUERY = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upcomingEventAdapter = EventAdapter()

        binding.upcomingEventRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = upcomingEventAdapter
        }

        viewModel.listEventsItemUpcoming.observe(viewLifecycleOwner) { events ->
            if (events != null) {
                upcomingEventAdapter.submitList(events)
            } else {
                Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.stored.observe(viewLifecycleOwner) { events ->
            if (events != null) {
                upcomingEventAdapter.submitList(events)
            } else {
                Toast.makeText(context, "No upcoming events found", Toast.LENGTH_SHORT).show()
            }
        }

        if (arguments?.containsKey(EXTRA_QUERY) == true) {
            val query = arguments?.getString(EXTRA_QUERY)
            if (query == "%default") {
                viewModel.stored.observe(viewLifecycleOwner) { events ->
                    if (events != null) {
                        upcomingEventAdapter.submitList(events)
                    } else {
                        Toast.makeText(context, "No upcoming events found", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                if (query != null) {
                    viewModel.searchEvents(query)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}