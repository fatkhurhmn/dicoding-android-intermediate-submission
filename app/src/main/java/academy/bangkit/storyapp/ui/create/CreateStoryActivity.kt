package academy.bangkit.storyapp.ui.create

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.databinding.ActivityCreateStoryBinding
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class CreateStoryActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private lateinit var binding: ActivityCreateStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbarCreate.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.btn_close_create -> {
                showCloseDialog()
                true
            }
            else -> false
        }
    }

    private fun showCloseDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.close))
            setMessage(getString(R.string.message_close))
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}