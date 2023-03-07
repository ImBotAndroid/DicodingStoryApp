package com.example.submissiondicodingintermediate_1.ui.uploadstory

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.submissiondicodingintermediate_1.R
import com.example.submissiondicodingintermediate_1.data.helper.uriToFile
import com.example.submissiondicodingintermediate_1.databinding.ActivityUploadBinding
import com.example.submissiondicodingintermediate_1.ui.ViewModelFactory
import com.example.submissiondicodingintermediate_1.ui.main.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var uploadViewModel: UploadViewModel
    private lateinit var currentPhotoPath: String

    private var getFile: File? = null

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Tidak Mendapatkan Izin Untuk Menggunakan Fitur.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpPermission()
        setUpAction()
        setUpViewModel()
        setUpView()
    }

    private fun setUpView() {
        supportActionBar?.title = resources.getString(R.string.story_app)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setUpViewModel() {
        uploadViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[UploadViewModel::class.java]
    }

    private fun setUpPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun setUpAction() {
        binding.btnCamera.setOnClickListener(this)
        binding.btnGalery.setOnClickListener(this)
        binding.btnUpload.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn_camera -> {
                openCamera()
            }
            R.id.btn_galery -> {
                openGallery()
            }
            R.id.btn_upload -> {
                uploadImage()
            }
        }
    }

    private fun uploadImage() {

        uploadViewModel.isLoading().observe(this){
            showLoading(it)
        }

        val preference = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val bearer = "Bearer " + preference.getString("token", "").toString()

        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.name, requestImageFile)

            val description = binding.edtDescription.text.toString().toRequestBody("text/plain".toMediaType())

            uploadViewModel.uploadStory(bearer, imageMultipart, description).observe(this){ upload ->
                if (upload.error){
                    AlertDialog.Builder(this).apply {
                        setMessage("Gagal Upload Story")
                        setPositiveButton("Oke") { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                    Toast.makeText(this, "Berhasil Upload Story", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity ::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }
        } else {
            Toast.makeText(this@UploadActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        com.example.submissiondicodingintermediate_1.data.helper.createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UploadActivity,
                "com.example.submissiondicodingintermediate_1",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)

            binding.ivImage.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@UploadActivity)
            getFile = myFile

            binding.ivImage.setImageURI(selectedImg)
        }
    }

    private fun reduceFileImage(file: File): File {
        return file
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarUpload.visibility = View.VISIBLE
        }else{
            binding.progressBarUpload.visibility = View.GONE
        }
    }
}