package com.duongnh.newtechnology2021demo.ui.beer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duongnh.newtechnology2021demo.R
import com.duongnh.newtechnology2021demo.base.BaseFragment
import com.duongnh.newtechnology2021demo.commons.LoadMoreHandler
import com.duongnh.newtechnology2021demo.commons.MyItemDecoration
import com.duongnh.newtechnology2021demo.commons.hide
import com.duongnh.newtechnology2021demo.commons.show
import com.duongnh.newtechnology2021demo.databinding.BeerFragmentBinding
import com.duongnh.newtechnology2021demo.ui.beer.adapter.BeerAdapter

class BeersFragment : BaseFragment() {

    //region Variables
    private var _binding: BeerFragmentBinding? = null
    override val binding get() = _binding!!
    private val viewModel: BeersViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private var layoutManagerBeer: LinearLayoutManager? = null
    private val beerAdapter: BeerAdapter by lazy {
        BeerAdapter()
    }
    private val totalItemInPage = 15
    private val loadMoreHandler: LoadMoreHandler by lazy {
        LoadMoreHandler(
            totalItemInPage = totalItemInPage,
            onScroll = { page -> viewModel.getBeers(page, totalItemInPage) })
    }
    //endregion

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BeerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initData() {
        viewModel.getBeers(1, totalItemInPage)
    }

    override fun bindComponent() {
        layoutManagerBeer = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recycleViewBeer.apply {
            layoutManager = layoutManagerBeer
            addItemDecoration(MyItemDecoration(resources.getDimension(R.dimen.bottom_item_decoration).toInt()))
            adapter = beerAdapter
        }
    }

    override fun bindEvent() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            bindData()
        }
        binding.recycleViewBeer.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val position = layoutManagerBeer?.findLastVisibleItemPosition() ?: 0
                if(!recyclerView.canScrollVertically(1))
                    loadMoreHandler.canScroll(position = position)
            }
        })
    }

    override fun bindData() {
        viewModel.beersViewState.observe(viewLifecycleOwner, { state ->
            handleBeersLoading(state)
            state.beers?.let { beers ->
                beerAdapter.add(beers){
                    loadMoreHandler.refresh(endPage = (beers.size < totalItemInPage))
                }
            }
            handleSearchError(state)
        })
    }

    private fun handleBeersLoading(state: BeersViewState) {
        if (state.isLoading) {
            binding.progressBar.show()
        } else {
            binding.progressBar.hide()
        }
    }

    private fun handleSearchError(state: BeersViewState) {
        state.error?.run {
            Toast.makeText(context, getString(this.message), Toast.LENGTH_LONG).show()
        }
    }
}