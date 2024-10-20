package com.example.aplikasidicodingeventnavigationdanapi.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasidicodingeventnavigationdanapi.EventAdapter
import com.example.aplikasidicodingeventnavigationdanapi.databinding.FragmentFinishedBinding
import com.example.aplikasidicodingeventnavigationdanapi.ui.upcoming.UpcomingFragment.Companion.EXTRA_QUERY

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FinishedViewModel by viewModels()

    private lateinit var finishedEventAdapter: EventAdapter

    companion object {
        const val EXTRA_QUERY2 = "0"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        finishedEventAdapter = EventAdapter()

        binding.finishedEventRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = finishedEventAdapter
        }

        viewModel.listEventsItemFinished.observe(viewLifecycleOwner) { events ->
            finishedEventAdapter.submitList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.stored.observe(viewLifecycleOwner) { events ->
            if (events != null) {
                finishedEventAdapter.submitList(events)
            } else {
                Toast.makeText(context, "No finished events found", Toast.LENGTH_SHORT).show()
            }
        }

        if (arguments?.containsKey(EXTRA_QUERY) == true) {
            val query = arguments?.getString(EXTRA_QUERY)
            if (query == "%default") {
                viewModel.stored.observe(viewLifecycleOwner) { events ->
                    if (events != null) {
                        finishedEventAdapter.submitList(events)
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