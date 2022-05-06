package academy.bangkit.storyapp.ui.main.home

import academy.bangkit.storyapp.adapter.ListStoryAdapter
import academy.bangkit.storyapp.adapter.LoadingStateAdapter
import academy.bangkit.storyapp.data.local.entity.Story
import academy.bangkit.storyapp.databinding.FragmentHomeBinding
import academy.bangkit.storyapp.databinding.StoryItemBinding
import academy.bangkit.storyapp.ui.create.CreateStoryActivity
import academy.bangkit.storyapp.ui.detail.StoryDetailActivity
import academy.bangkit.storyapp.ui.main.MainActivity
import academy.bangkit.storyapp.utils.Extension.showMessage
import academy.bangkit.storyapp.utils.SpacesItemDecoration
import academy.bangkit.storyapp.utils.ViewModelFactory
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val listStoryAdapter: ListStoryAdapter by lazy { ListStoryAdapter() }
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(
            requireContext()
        )
    }

    private val launcherCreateStoryIntent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == MainActivity.RESULT_CREATE && it.data != null) {
                val isError = it.data!!.getBooleanExtra(MainActivity.EXTRA_ERROR, true)
                if (!isError) {
                    getListStories()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingStateHandling()
        showListStory()
        getListStories()
        createStory()
        actionToDetail()
    }

    private fun loadingStateHandling() {
        listStoryAdapter.addLoadStateListener { loadSate ->
            when (loadSate.source.refresh) {
                is LoadState.Loading -> {
                    binding?.progressBarHome?.visibility = View.VISIBLE
                }

                is LoadState.NotLoading -> {
                    binding?.progressBarHome?.visibility = View.GONE
                }

                is LoadState.Error -> {
                    binding?.imgError?.visibility = View.VISIBLE
                    binding?.root?.let { view -> loadSate.toString().showMessage(view) }
                }
            }
        }
    }

    private fun showListStory() {
        binding?.rvStory?.apply {
            addItemDecoration(SpacesItemDecoration(16))

            layoutManager =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    LinearLayoutManager(context)
                } else {
                    GridLayoutManager(context, 2)
                }

            adapter = listStoryAdapter
            adapter = listStoryAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    listStoryAdapter.retry()
                }
            )
        }
    }

    private fun getListStories() {
        val token = arguments?.getString(MainActivity.EXTRA_TOKEN)
        if (token != null) {
            homeViewModel.getAllStory("Bearer $token").observe(viewLifecycleOwner) { result ->
                listStoryAdapter.submitData(lifecycle, result)
            }
        }
    }

    private fun createStory() {
        val token = arguments?.getString(MainActivity.EXTRA_TOKEN)
        binding?.fabAddStory?.setOnClickListener {
            val createStoryIntent = Intent(context, CreateStoryActivity::class.java)
            createStoryIntent.putExtra(MainActivity.EXTRA_TOKEN, token)
            launcherCreateStoryIntent.launch(createStoryIntent)
        }
    }

    private fun actionToDetail() {
        listStoryAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(
                story: Story,
                view: StoryItemBinding,
                itemView: View
            ) {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        androidx.core.util.Pair(view.imgStoryPhoto, "photo"),
                        androidx.core.util.Pair(view.tvStoryName, "name")
                    )
                val detailIntent = Intent(context, StoryDetailActivity::class.java)
                detailIntent.putExtra(StoryDetailActivity.EXTRA_DETAIL, story)
                startActivity(detailIntent, optionsCompat.toBundle())
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}