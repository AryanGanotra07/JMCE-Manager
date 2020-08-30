package com.aryanganotra.jmcemanager.activities.notes

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aryanganotra.jmcemanager.R
import com.aryanganotra.jmcemanager.application.AppApplication
import com.aryanganotra.jmcemanager.model.Note
import com.aryanganotra.jmcemanager.utils.toast
import kotlinx.android.synthetic.main.activity_edit_note.*
import java.io.File
import java.util.concurrent.Executors


class EditNoteActivity : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: Int = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        supportActionBar?.setTitle("Edit Note")
        val bundle = intent.getBundleExtra("note")
        val note = bundle.getParcelable<Note>("note")
        name_et.setText(note?.noteName)
        url_et.setText(note?.downloadUrl)
        cancel_btn.setOnClickListener {
            val returnIntent = Intent()
            setResult(Activity.RESULT_CANCELED, returnIntent)
            finish()
        }
        update_btn.setOnClickListener {
            if (!url_et.text.isNullOrEmpty())
            {
                note?.noteName = name_et.text.toString()
                note?.downloadUrl = url_et.text.toString()
                val bundle = Bundle()
                bundle.putParcelable("note",note)
                val returnIntent = Intent()
                returnIntent.putExtra("note",bundle)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()

            }
            else
            {
                url_et.setError("Can't be empty")
            }
        }

        download_iv.setOnClickListener {
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
                val filename = getFileName(url)
                val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
                val executors = Executors.newCachedThreadPool()
                val manager =
                    application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                request.setTitle(filename)
                request.setDescription(application.resources.getString(R.string.app_name))
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    filename
                )
                try {
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

    private fun getFileName(url : String) : String{
        val uri = Uri.parse(url)
        val uriString = uri.toString()
        val myFile = File(uriString)
        val path: String = myFile.getAbsolutePath()
        var displayName: String = System.currentTimeMillis().toString()

        if (uriString.startsWith("content://")) {
            var cursor: Cursor? = null
            try {
                cursor = this.getContentResolver().query(uri, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    displayName =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        } else if (uriString.startsWith("file://")) {
            displayName = myFile.getName()
        }

        return displayName+".pdf"

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {

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
