package com.aryanganotra.jmcemanager.activities

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import com.aryanganotra.jmcemanager.R
import com.aryanganotra.jmcemanager.application.AppApplication
import com.aryanganotra.jmcemanager.model.Note
import com.aryanganotra.jmcemanager.utils.toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_note.*
import java.lang.Exception
import java.net.URI
import java.util.concurrent.Executors
import kotlin.math.roundToInt


class AddNoteActivity : AppCompatActivity() {

    private val PICK_PDF_CODE: Int = 2
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: Int = 1
    private val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: Int = 2
    private val MY_PERMISSIONS_REQUEST_READ_DO_NOTHING_EXTERNAL_STORAGE: Int = 3

    private val STORAGE_PATH_UPLOADS = "uploads/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        supportActionBar?.setTitle("Add Note")
        var uri : Uri
        val sub_id = intent.getStringExtra("sub_id")
        val course_name = intent.getStringExtra("course_name")
        val uriString = intent.getStringExtra("uri")
        if (uriString!=null)
        {
            url_et.setText(uriString)
            url_et.isEnabled = false
            rg.visibility = View.GONE
            rg.isEnabled = false
            url_rb.isEnabled = false
            upload_rb.isEnabled =false
            save_btn.isClickable = false
            save_btn.isEnabled = false

           uri =  Uri.parse(uriString)

                uploadFileToFirebase(uri)
        }
        course_name_tv.setText(course_name)
        rg.setOnCheckedChangeListener { radioGroup, i ->
            val checkedRadioButton = radioGroup.findViewById(i) as RadioButton
            val isChecked = checkedRadioButton.isChecked
            if (isChecked) {
               when(checkedRadioButton.id)
               {
                   R.id.url_rb -> {
                       url_et.isEnabled = true
                       save_btn.isClickable = true
                       save_btn.isEnabled = true
                   }
                   R.id.upload_rb -> {
                       url_et.isEnabled = false
                       save_btn.isClickable = false
                       save_btn.isEnabled = false
                       uploadFile()

                   }
               }
            }
        }

        cancel_btn.setOnClickListener {

            val returnIntent = Intent()
            setResult(Activity.RESULT_CANCELED, returnIntent)
            finish()
        }

        save_btn.setOnClickListener {
            if (name_et.text!=null && name_et.text.length>0 && url_et.text!=null && url_et.text.length>0)
            {
                val note : Note = Note()
                note.downloadUrl = url_et.text.toString()
                note.subId = sub_id
                note.noteName = name_et.text.toString()
                val bundle = Bundle()
                bundle.putParcelable("note", note)
                val returnIntent = Intent()
                returnIntent.putExtra("note", bundle)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            else
            {
                if (name_et.text.isNotEmpty() && url_et.text.isNullOrEmpty())
                {
                    url_et.setError("Can't be empty")
                }
                else if (url_et.text.isNotEmpty() && name_et.text.isNullOrEmpty())
                {
                    name_et.setError("Can't be empty")
                }
                else
                {
                    name_et.setError("Can't be empty")
                    name_et.setError("Can't be empty")
                }
            }
        }

        download_btn.setOnClickListener {
            if (!url_et.text.isNullOrEmpty())
            {
                startDownload(url_et.text.toString())
            }
            else
            {
                url_et.setError("Can't be empty")
            }
        }



    }

    private fun startDownload (url : String){
        if (checkStoragePermission(MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)) {
            try {


                val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
                val executors = Executors.newCachedThreadPool()
                val manager =
                    application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                request.setTitle(name_et.text?.toString())
                request.setDescription(application.resources.getString(R.string.app_name))
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "${System.currentTimeMillis()}"
                )
                try {
                    AppApplication?.context?.toast("Download Queued")
                    executors.execute {

                        try {

                            manager.enqueue(request)
                        } catch (e: Exception) {

                        }

                    }
                } catch (e: Exception) {
                    AppApplication?.context?.toast(e.message.toString())
                }
            }
            catch (e : Exception)
            {
                AppApplication?.context?.toast(e.message.toString())
            }


        }

    }


    private fun uploadFile()
    {
        if (checkStoragePermission(MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE))
        {
            val intent = Intent()
            intent.type = "application/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Note"), PICK_PDF_CODE)
        }
    }

    private fun checkStoragePermission(REQUEST_CODE : Int) : Boolean
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
//
            return true
        }
        else{
//            if (shouldShowRequestPermissionRationale(
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                // Explain to the user why we need to read the contacts
//            }
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE)
        }
        return false
    }

    private fun checkStoragePermission() : Boolean
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
//
            return true
        }
        else{
//            if (shouldShowRequestPermissionRationale(
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                // Explain to the user why we need to read the contacts
//            }
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_DO_NOTHING_EXTERNAL_STORAGE
                )
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                progressBar.visibility = View.VISIBLE
                uploadFileToFirebase(data.getData()!!);
            }else{
                AppApplication?.context?.toast("No Application Chosen")
            }
        }
    }

    private fun uploadFileToFirebase(data: Uri) {

        if (checkStoragePermission()) {
            val sRef: StorageReference =
                FirebaseStorage.getInstance()
                    .reference.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".pdf")
            sRef.putFile(data)
                .addOnSuccessListener {

                    sRef.downloadUrl.addOnCompleteListener {
                        progressBar.setVisibility(View.GONE)
                        textViewStatus.setText("File Uploaded Successfully")
                        save_btn.isClickable = true
                        save_btn.isEnabled = true
                        url_et.setText(it.result.toString())
                    }
                }
                .addOnFailureListener(OnFailureListener { exception ->
                    progressBar.setVisibility(View.GONE)
                    textViewStatus.setText(exception.message)

                })
                .addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot?> {
                    override fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) {
                        val progress: Double =
                            100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()
                        textViewStatus.setText(progress.roundToInt().toString() + "% Uploading " + data?.encodedPath)
                    }
                })
        }
        else
        {
            uploadFileToFirebase(data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    uploadFile()

                } else {
                    AppApplication?.context?.toast("Please give storage permission to upload notes")
                }
                return
            }
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (!url_et.text.isNullOrEmpty())
                    {
                        startDownload(url_et.text.toString())
                    }
                    else
                    {
                        url_et.setError("Can't be empty")
                    }

                } else {
                    AppApplication?.context?.toast("Please give storage permission to download notes")
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
