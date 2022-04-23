package academy.bangkit.storyapp.ui.main

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.adapter.ListStoryAdapter
import academy.bangkit.storyapp.data.Result
import academy.bangkit.storyapp.data.remote.response.StoryResponse
import academy.bangkit.storyapp.databinding.ActivityMainBinding
import academy.bangkit.storyapp.databinding.StoryItemBinding
import academy.bangkit.storyapp.ui.auth.AuthenticationActivity
import academy.bangkit.storyapp.ui.create.CreateStoryActivity
import academy.bangkit.storyapp.ui.detail.StoryDetailActivity
import academy.bangkit.storyapp.utils.Extension.showMessage
import academy.bangkit.storyapp.utils.SpacesItemDecoration
import academy.bangkit.storyapp.utils.ViewModelFactory
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val listStoryAdapter: ListStoryAdapter by lazy { ListStoryAdapter() }
    private val mainViewModel: MainViewModel by viewModels { ViewModelFactory.getInstance(this) }

    private val launcherCreateStoryIntent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_CREATE && it.data != null) {
                val isError = it.data!!.getBooleanExtra(EXTRA_ERROR, true)
                if (!isError) {
                    getListStories()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        getListStories()
        createStory()
        actionToDetail()
    }

    private fun getListStories() {
        val token = intent.getStringExtra(EXTRA_TOKEN)

        if (token != null) {
            mainViewModel.getAllStory("Bearer $token").observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBarMain.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBarMain.visibility = View.GONE
                        val stories = result.data.storyResponses
                        if (!result.data.error) {
                            listStoryAdapter.setStories(stories as ArrayList<StoryResponse>)
                            showListStory()
                        }
                    }

                    is Result.Error -> {
                        with(binding) {
                            progressBarMain.visibility = View.GONE
                            rvStory.visibility = View.GONE
                            imgError.visibility = View.VISIBLE
                        }
                        result.error.showMessage(binding.root)
                    }
                }
            }
        }
    }

    private fun showListStory() {
        with(binding.rvStory) {
            addItemDecoration(SpacesItemDecoration(16))
            layoutManager =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    LinearLayoutManager(this@MainActivity)
                } else {
                    GridLayoutManager(this@MainActivity, 2)
                }
            adapter = listStoryAdapter
            setHasFixedSize(true)
        }
    }

    private fun createStory() {
        val token = intent.getStringExtra(EXTRA_TOKEN)
        binding.fabAddStory.setOnClickListener {
            val createStoryIntent = Intent(this, CreateStoryActivity::class.java)
            createStoryIntent.putExtra(EXTRA_TOKEN, token)
            launcherCreateStoryIntent.launch(createStoryIntent)
        }
    }

    private fun actionToDetail() {
        listStoryAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(storyResponse: StoryResponse, view: StoryItemBinding, itemView:View) {
                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    androidx.core.util.Pair(view.imgStoryPhoto, "photo"),
                    androidx.core.util.Pair(view.tvStoryName, "name")
                )
                val detailIntent = Intent(this@MainActivity, StoryDetailActivity::class.java)
                detailIntent.putExtra(StoryDetailActivity.EXTRA_DETAIL, storyResponse)
                startActivity(detailIntent, optionsCompat.toBundle())
            }

        })
    }

    private fun setupToolbar() {
        binding.toolbarMain.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.btn_logout -> {
                showLogoutDialog()
                true
            }

            R.id.btn_locale->{
                val localeIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(localeIntent)
                true
            }
            else -> false
        }
    }

    private fun showLogoutDialog() {
        mainViewModel.deleteSession()

        val alertDialogBuilder = AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.logout))
            setMessage(getString(R.string.message_logout))
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                val authIntent = Intent(this@MainActivity, AuthenticationActivity::class.java)
                startActivity(authIntent)
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        const val EXTRA_TOKEN = "academy.bangkit.storyapp.EXTRA_TOKEN"
        const val RESULT_CREATE = 100
        const val EXTRA_ERROR = "academy.bangkit.storyapp.ERROR"
    }
}