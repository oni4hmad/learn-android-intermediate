package com.dicoding.picodiploma.storyapp2.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.dicoding.picodiploma.storyapp2.R
import com.dicoding.picodiploma.storyapp2.data.preferences.SessionPreference
import com.dicoding.picodiploma.storyapp2.databinding.FragmentAddStoryBinding
import com.dicoding.picodiploma.storyapp2.utils.createCustomTempFile
import com.dicoding.picodiploma.storyapp2.utils.reduceFileImage
import com.dicoding.picodiploma.storyapp2.utils.rotateBitmap
import com.dicoding.picodiploma.storyapp2.utils.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class AddStoryFragment : Fragment() {

    private lateinit var binding: FragmentAddStoryBinding
    private lateinit var viewModel: AddStoryViewModel
    private lateinit var session: SessionPreference

    private lateinit var currentPhotoPath: String

    private var getFile: File? = null

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    /* permission */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission((requireActivity() as AddStoryActivity).baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session = SessionPreference(view.context)
        val token = session.getAuthToken() ?: ""

        viewModel = ViewModelProvider(this, AddStoryViewModelFactory(token))[AddStoryViewModel::class.java]
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        viewModel.isSucceed.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { isSucceed ->
                if (isSucceed) toStoryList(getString(R.string.story_created_successfully))
            }
        }
        viewModel.toastText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { toastText ->
                showToast(toastText)
            }
        }
        viewModel.file.observe(viewLifecycleOwner) {
            it?.let {
                getFile = it
                val imageBmp = BitmapFactory.decodeFile(it.path)
                binding.ivImgPreview.setImageBitmap(imageBmp)
            }
        }

        /* permission */
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                (requireActivity() as AddStoryActivity),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadImage() }
    }

    private fun toStoryList(msg: String) {
        val toStoryActivity = AddStoryFragmentDirections.actionAddStoryFragmentToStoryActivity()
        toStoryActivity.toastText = msg

        val extras = ActivityNavigator.Extras.Builder()
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .build()

        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .build()

        view?.findNavController()?.navigate(toStoryActivity.actionId, toStoryActivity.arguments, navOptions, extras)
    }

    private fun uploadImage() {
        if (getFile != null) {

            if (binding.edtDescription.text.toString().isEmpty()) {
                binding.edtDescription.error = getString(R.string.description_cant_be_empty)
                return
            }

            lifecycleScope.launch(Dispatchers.Default) {
                //simulate process in background thread
                withContext(Dispatchers.Main) {
                    showLoading(true)
                }

                val file = reduceFileImage(getFile as File)

                val description = binding.edtDescription.text.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                viewModel.uploadImage(imageMultipart, description)
            }

        } else {
            Toast.makeText(view?.context, getString(R.string.error_no_image), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTakePhoto() {
        view?.let { v ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity((requireActivity() as AddStoryActivity).packageManager)

            createCustomTempFile((requireActivity() as AddStoryActivity).application).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    v.context,
                    "com.dicoding.picodiploma.storyapp2",
                    it
                )
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_a_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {

            val myFile = File(currentPhotoPath)

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                true
            )

            result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(myFile))
            viewModel.setFile(myFile)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            view?.let { uriToFile(selectedImg, it.context) }?.also {
                viewModel.setFile(it)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnCamera.isEnabled = false
            binding.btnGallery.isEnabled = false
            binding.btnUpload.isEnabled = false
            binding.edtDescription.isEnabled = false
            binding.pbAddStory.visibility = View.VISIBLE
        }
        else {
            binding.btnCamera.isEnabled = true
            binding.btnGallery.isEnabled = true
            binding.btnUpload.isEnabled = true
            binding.edtDescription.isEnabled = true
            binding.pbAddStory.visibility = View.GONE
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(view?.context, text, Toast.LENGTH_SHORT).show()
    }

}
