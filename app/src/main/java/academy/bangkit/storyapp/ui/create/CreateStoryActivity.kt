package academy.bangkit.storyapp.ui.create

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.databinding.ActivityCreateStoryBinding
import academy.bangkit.storyapp.utils.MediaHelper
import academy.bangkit.storyapp.utils.MediaHelper.uriToFile
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import java.io.File

class CreateStoryActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private lateinit var binding: ActivityCreateStoryBinding
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    private val launcherIntentCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                binding.imgPreview.setPadding(15, 30, 15, 30)
                val myFile = File(currentPhotoPath)
                getFile = myFile
                val resultPhoto = BitmapFactory.decodeFile(myFile.path)
                binding.imgPreview.setImageBitmap(resultPhoto)
            }
        }

    private val launchIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                binding.imgPreview.setPadding(15, 30, 15, 30)
                val selectedImg: Uri = it.data?.data as Uri
                val myFile = uriToFile(selectedImg, this)
                getFile = myFile
                binding.imgPreview.setImageURI(selectedImg)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        takeImageFromCamera()
        takeImageFromGallery()
    }

    private fun takeImageFromGallery() {
        binding.btnToGallery.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launchIntentGallery.launch(chooser)
        }
    }

    private fun takeImageFromCamera() {
        binding.btnToCamera.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)

            MediaHelper.createTempFile(application).also {
                val photoUri: Uri = FileProvider.getUriForFile(
                    this,
                    "academy.bangkit.storyapp",
                    it
                )
                currentPhotoPath = it.absolutePath
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                launcherIntentCamera.launch(cameraIntent)
            }
        }
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