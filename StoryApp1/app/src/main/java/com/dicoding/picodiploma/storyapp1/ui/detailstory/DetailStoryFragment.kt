package com.dicoding.picodiploma.storyapp1.ui.detailstory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.storyapp1.data.network.StoryItem
import com.dicoding.picodiploma.storyapp1.databinding.FragmentDetailStoryBinding

class DetailStoryFragment : Fragment() {

    private lateinit var binding: FragmentDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = context?.let { TransitionInflater.from(it).inflateTransition(android.R.transition.move) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DetailStoryActivityArgs.fromBundle(arguments as Bundle).story.let {
            showStory(it)
        }
    }

    private fun showStory(story: StoryItem) {
        (requireActivity() as DetailStoryActivity).supportActionBar?.title = "${story.name}'s story"
        Glide.with(binding.ivStory.context)
            .load(story.photoUrl)
            .into(binding.ivStory)

        binding.tvDescription.text = story.description
    }

    private fun showToast(text: String) {
        Toast.makeText(view?.context, text, Toast.LENGTH_SHORT).show()
    }

}