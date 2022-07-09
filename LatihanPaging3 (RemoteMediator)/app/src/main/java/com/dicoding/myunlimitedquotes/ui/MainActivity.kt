package com.dicoding.myunlimitedquotes.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myunlimitedquotes.adapter.LoadingStateAdapter
import com.dicoding.myunlimitedquotes.adapter.QuoteListAdapter
import com.dicoding.myunlimitedquotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvQuote.layoutManager = LinearLayoutManager(this)

        getData()
    }

    private fun getData() {
        val adapter = QuoteListAdapter()
        adapter.addLoadStateListener {
            when (it.mediator?.refresh) {
                is LoadState.Loading -> {
                    showLoading(true)
                    showRefresh(false)
                    showToast("Loading!")
                }
                is LoadState.Error -> {
                    showLoading(false)
                    if (adapter.itemCount < 1) {
                        showRefresh(true)
                        (it.mediator?.refresh as LoadState.Error).error.message?.also { errorMsg ->
                            binding.tvNoData.text = errorMsg
                        }
                        binding.btnRefresh.setOnClickListener { adapter.refresh() }
                    } else showRefresh(false)
                    showToast("Error!")
                }
                is LoadState.NotLoading -> {
                    showLoading(false)
                    if (adapter.itemCount < 1) {
                        showRefresh(true)
                        binding.tvNoData.text = "Tidak ada data"
                        binding.btnRefresh.setOnClickListener { adapter.refresh() }
                    } else showRefresh(false)
                    showToast("Not Loading!")
                }
            }
        }
        binding.rvQuote.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.quote.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbQuotes.visibility = when (isLoading) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

    private fun showRefresh(isShow: Boolean) {
        binding.rlNoDataRefresh.visibility = when (isShow) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}