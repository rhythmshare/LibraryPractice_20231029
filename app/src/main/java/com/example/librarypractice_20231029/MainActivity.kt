package com.example.librarypractice_20231029

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.librarypractice_20231029.databinding.ActivityMainBinding
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import android.Manifest

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.imgProfile.setOnClickListener {
//            ImageView도 클릭 이벤트 지원
//            ViewPhotoActivity로 이동
            val myIntent = Intent(this, ViewPhotoActivity::class.java)
            startActivity(myIntent)
        }

        binding.btnCall.setOnClickListener {
//            전화를 바로 걸기 (번호만 눌린 화면 X, 신호까지 가도록
//            바로 걸기 (CALL) => 사용자 요금 사용: 권한 허가 후에 가능.
//            권한 획득 성공 여부에 따른 행동 지침을 변수에 저장(인터페이스 => 객체화 : 익명클래스 활용)
            val pl = object : PermissionListener{
                override fun onPermissionGranted() {
//                    권한 허가 되었을때 할 일 => 전화 걸기
                    val myUri = Uri.parse("tel:010-3333-5555")

                    val myIntent = Intent(Intent.ACTION_CALL, myUri)
                    startActivity(myIntent)
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
//                    거절되면 => 토스트로 안내
                    Toast.makeText(this@MainActivity, "권한 문제로 전화 연결이 안 됩니다.", Toast.LENGTH_SHORT).show()
                }
            }

//            실제로 권한을 물어보자
            TedPermission.create()
                .setPermissionListener(pl)
                .setDeniedMessage("권한을 주지 않으면 통화가 안 됩니다.")
                .setPermissions(Manifest.permission.CALL_PHONE,Manifest.permission.INTERNET)
                .check()

        }
        //            화면이 켜지면 바로 이미지 불러내기 (웹상 이미지)
        Glide.with(this).load("https://www.sydneyoperahouse.com/sites/default/files/styles/3x1__2560w/public/collaborodam_assets/AndrewHuberman_2560x854_syd.jpg.webp?itok=uKO3tkaW").into(binding.imgThumbnail)
    }
}