package com.dicoding.picodiploma.storyapp1.ui.detailstory

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.storyapp1.data.network.StoryItem
import com.dicoding.picodiploma.storyapp1.databinding.FragmentDetailStoryBinding

class DetailStoryFragment : Fragment() {

    private lateinit var binding: FragmentDetailStoryBinding
    private lateinit var viewModel: DetailStoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(DetailStoryViewModel::class.java)

        DetailStoryActivityArgs.fromBundle(arguments as Bundle).story.let {
            showStory(it)
        }
    }

    private fun showStory(story: StoryItem) {
        Glide.with(binding.ivStory.context)
            .load(story.photoUrl)
            .into(binding.ivStory)

        binding.tvDescription.text = story.description
    }

}