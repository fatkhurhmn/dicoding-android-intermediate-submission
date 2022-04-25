package academy.bangkit.storyapp.ui.create

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.data.Result
import academy.bangkit.storyapp.databinding.ActivityCreateStoryBinding
import academy.bangkit.storyapp.ui.main.MainActivity
import academy.bangkit.storyapp.utils.Extension.showMessage
import academy.bangkit.storyapp.utils.MediaHelper
import academy.bangkit.storyapp.utils.MediaHelper.uriToFile
import academy.bangkit.storyapp.utils.ViewModelFactory
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CreateStoryActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private lateinit var binding: ActivityCreateStoryBinding
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private var latitude: RequestBody? = null
    private var longitude: RequestBody? = null
    private val createStoryViewModel: CreateStoryViewModel by viewModels {
        ViewModelFactory.getInstance(
            this
        )
    }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                getCurrentLocation()
            }
        }

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
        uploadStory()
        setLocation()
    }

    private fun setLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setupLoading(true)
                getCurrentLocation()
            }else{
                latitude = null
                longitude = null
            }
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                setupLoading(false)
                if (location != null) {
                    latitude =
                        location.latitude.toString().toRequestBody("text/plain".toMediaType())
                    longitude =
                        location.longitude.toString().toRequestBody("text/plain".toMediaType())
                    getString(R.string.success_get_current_location).showMessage(binding.root)
                } else {
                    binding.switchLocation.isChecked = false
                    getString(R.string.failed_get_current_location).showMessage(binding.root)
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun uploadStory() {
        val token = intent.getStringExtra(MainActivity.EXTRA_TOKEN)
        binding.btnUpload.setOnClickListener {
            val description = binding.edtDescription.text.toString().trim()

            if (getFile == null) {
                getString(R.string.take_image_first).showMessage(binding.root)
            } else if (description.isEmpty()) {
                getString(R.string.fill_desc).showMessage(binding.root)
            } else {

                val desc = description.toRequestBody("text/plain".toMediaType())
                val file = MediaHelper.reduceFileImage(getFile as File)
                val imageMultipart = MediaHelper.fileToImageMultipart(file)
                if (token != null) {
                    createStoryViewModel.uploadNewStory(
                        token,
                        imageMultipart,
                        desc,
                        latitude,
                        longitude
                    )
                        .observe(this) { result ->
                            when (result) {
                                is Result.Loading -> {
                                    setupLoading(true)
                                }

                                is Result.Success -> {
                                    setupLoading(false)
                                    val message = result.data.message
                                    val isError = result.data.error
                                    message.showMessage(binding.root)
                                    if (!isError) {
                                        val resultIntent = Intent()
                                        resultIntent.putExtra(MainActivity.EXTRA_ERROR, isError)
                                        setResult(MainActivity.RESULT_CREATE, resultIntent)
                                        finish()
                                    }
                                }

                                is Result.Error -> {
                                    setupLoading(false)
                                    result.error.showMessage(binding.root)
                                }
                            }
                        }
                }
            }
        }
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
        val description = binding.edtDescription.text.toString()
        return when (item?.itemId) {
            R.id.btn_close_create -> {
                if (description.isNotEmpty() || getFile != null) {
                    showCloseDialog()
                } else {
                    finish()
                }
                true
            }
            else -> false
        }
    }

    override fun onBackPressed() {
        val description = binding.edtDescription.text.toString()
        if (description.isNotEmpty() || getFile != null) {
            showCloseDialog()
        } else {
            finish()
        }
    }

    private fun setupLoading(isLoading: Boolean) {
        with(binding) {
            btnToCamera.isClickable = !isLoading
            btnToGallery.isClickable = !isLoading
            btnUpload.isClickable = !isLoading
        }
        binding.progressBarCreate.visibility = if (isLoading) View.VISIBLE else View.GONE
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