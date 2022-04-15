package academy.bangkit.storyapp.ui.main

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.adapter.ListStoryAdapter
import academy.bangkit.storyapp.data.Result
import academy.bangkit.storyapp.databinding.ActivityMainBinding
import academy.bangkit.storyapp.ui.auth.AuthenticationActivity
import academy.bangkit.storyapp.utils.Extension.showMessage
import academy.bangkit.storyapp.utils.SpacesItemDecoration
import academy.bangkit.storyapp.utils.ViewModelFactory
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: ViewModelFactory
    private val listStoryAdapter: ListStoryAdapter by lazy { ListStoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        setupToolbar()
        getListStories()
    }

    private fun getListStories() {
        val mainViewModel: MainViewModel by viewModels { factory }
        val token = intent.getStringExtra(EXTRA_TOKEN)

        if (token != null) {
            mainViewModel.getAllStory("Bearer $token").observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBarMain.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBarMain.visibility = View.GONE
                        val stories = result.data.stories
                        if (!result.data.error) {
                            listStoryAdapter.submitList(stories)
                            Log.d("CEK", "getListStories1: $stories")
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

    private fun setupToolbar() {
        binding.toolbarMain.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.btn_logout -> {
                showLogoutDialog()
                true
            }
            else -> false
        }
    }

    private fun showLogoutDialog() {
        val mainViewModel: MainViewModel by viewModels { factory }
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
    }
}