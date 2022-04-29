package com.dicoding.picodiploma.storyapp1.ui.liststory

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.storyapp1.data.network.StoryItem
import com.dicoding.picodiploma.storyapp1.data.preferences.SessionPreference
import com.dicoding.picodiploma.storyapp1.databinding.FragmentListStoryBinding
import com.dicoding.picodiploma.storyapp1.databinding.ItemRowStoryBinding

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
            showRecyclerList(stories)
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isError) error.errorMsg?.let { showError(true, it) }
            else showError(false)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.btnLoad.setOnClickListener {
            loadStory(token)
        }

        binding.btnRetry.setOnClickListener {
            loadStory(token)
        }

        binding.srlStories.setOnRefreshListener {
            loadStory(token)
        }

        StoryActivityArgs.fromBundle(arguments as Bundle).toastText?.let {
            showToast(it)
            loadStory(token)
            arguments?.clear()
        }

    }

    private fun loadStory(token: String) {
        if (binding.edtStoryQty.text.toString().isNotEmpty()) {
            val qty = binding.edtStoryQty.text.toString().toInt()
            viewModel.getStory(token, size = qty)
        } else binding.edtStoryQty.error = "Can't be empty"
    }

    private fun showRecyclerList(stories: List<StoryItem>) {

        binding.rvStories.layoutManager = LinearLayoutManager(view?.context)

        val listStoryAdapter = ListStoryAdapter(stories)
        binding.rvStories.adapter = listStoryAdapter

        listStoryAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(story: StoryItem, imgView: ImageView) {
                showStory(story, imgView)
            }
        })
    }

    private fun showStory(story: StoryItem, imgView: ImageView) {
        val extras = FragmentNavigatorExtras(
            imgView to "imageView")
        val toDetailStoryFragment = ListStoryFragmentDirections.actionListStoryFragmentToDetailStoryActivity(story)
        view?.findNavController()?.navigate(toDetailStoryFragment.actionId,
            toDetailStoryFragment.arguments, // Bundle of args
            null, // NavOptions
            extras)

        /*val toDetailStoryFragment = ListStoryFragmentDirections.actionListStoryFragmentToDetailStoryActivity(story)
        view?.findNavController()?.navigate(toDetailStoryFragment)*/
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.tvPlcStoryperpage.isEnabled = false
            binding.edtStoryQty.isEnabled = false
            binding.btnLoad.isEnabled = false
            binding.srlStories.isRefreshing = true
            binding.rvStories.visibility = View.GONE
        } else {
            binding.tvPlcStoryperpage.isEnabled = true
            binding.edtStoryQty.isEnabled = true
            binding.btnLoad.isEnabled = true
            binding.srlStories.isRefreshing = false
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(view?.context, text, Toast.LENGTH_SHORT).show()
    }

    private fun showError(isError: Boolean, msg: String = "") {
        if (isError) {
            binding.tvErrorMsg.text = msg
            binding.rvStories.visibility = View.GONE
            binding.tvErrorMsg.visibility = View.VISIBLE
            binding.btnRetry.visibility = View.VISIBLE
        } else {
            binding.rvStories.visibility = View.VISIBLE
            binding.tvErrorMsg.visibility = View.GONE
            binding.btnRetry.visibility = View.GONE
        }
    }
}