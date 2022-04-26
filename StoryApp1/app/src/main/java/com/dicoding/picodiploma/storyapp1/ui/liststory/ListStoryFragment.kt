package com.dicoding.picodiploma.storyapp1.ui.liststory

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.storyapp1.data.network.StoryItem
import com.dicoding.picodiploma.storyapp1.data.preferences.SessionPreference
import com.dicoding.picodiploma.storyapp1.databinding.FragmentListStoryBinding
import com.dicoding.picodiploma.storyapp1.ui.register.RegisterFragmentDirections

class ListStoryFragment : Fragment() {

    private lateinit var binding: FragmentListStoryBinding
    private lateinit var viewModel: ListStoryViewModel
    private lateinit var session: SessionPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session = SessionPreference(view.context)
        val token = session.getAuthToken() ?: ""

        viewModel = ViewModelProvider(this, ListStoryViewModelFactory(token))[ListStoryViewModel::class.java]
        viewModel.listStory.observe(viewLifecycleOwner) { stories ->
            /*stories.forEach {
                binding.tvTest.append(" | ${it.id} ${it.name} ${it.description} ${it.photoUrl} ${it.createdAt} ${it.lat} ${it.lon}")
            }*/
            showRecyclerList(stories)
        }
        viewModel.toastText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { toastText ->
                showToast(toastText)
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

    }

    private fun showRecyclerList(stories: List<StoryItem>) {

        binding.rvStories.layoutManager = LinearLayoutManager(view?.context)

        val listStoryAdapter = ListStoryAdapter(stories)
        binding.rvStories.adapter = listStoryAdapter

        listStoryAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(story: StoryItem) {
                showStory(story)
                /*story.let {
                    showToast("${it.id} ${it.name} ${it.description} ${it.photoUrl} ${it.createdAt} ${it.lat} ${it.lon}")
                }*/
            }
        })
    }

    private fun showStory(story: StoryItem) {
        val toDetailStoryFragment = ListStoryFragmentDirections.actionListStoryFragmentToDetailStoryActivity(story)
        view?.findNavController()?.navigate(toDetailStoryFragment)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.tvTest.isEnabled = false
            binding.pbListStory.visibility = View.VISIBLE
        } else {
            binding.tvTest.isEnabled = true
            binding.pbListStory.visibility = View.GONE
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(view?.context, text, Toast.LENGTH_SHORT).show()
    }
}