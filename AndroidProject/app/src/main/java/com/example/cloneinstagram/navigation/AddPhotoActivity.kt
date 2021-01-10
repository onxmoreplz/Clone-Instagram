package com.example.cloneinstagram.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cloneinstagram.R
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {

    var PICK_IMAGE_FROM__ALBUM = 0

    var storage: FirebaseStorage? = null
    var photoUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        //Storage 초기화
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM__ALBUM)

        //Upload 버튼 이벤트 등록
        addphoto_btn_upload.setOnClickListener {
            contentUpload()
        }
    }

    private fun contentUpload() {
        var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timeStamp + "_.png"

        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        // 파일 업로드
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            Toast.makeText(this, getString(R.string.upload_image), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM__ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                // 사진 선택 시, 이미지 경로가 넘어옴
                photoUri = data?.data
                iv_add_photo_image.setImageURI(photoUri)

            } else {
                // [취소] 버튼 눌렀을 때
                finish()
            }
        }
    }
}