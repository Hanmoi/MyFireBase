package com.example.myfirebase

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myfirebase.databinding.ActivityBoardWriteBinding
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BoardWriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityBoardWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 수정 모드인지 확인 (BoardList에서 전달받은 데이터가 있는지 체크)
        val key = intent.getStringExtra("key")
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")

        if (key != null) {
            binding.writeTextArea.setText(title)
            binding.writeContentArea.setText(content)
            binding.writeUploadBtn.text = "수정하기"
            binding.writeDeleteBtn.visibility = View.VISIBLE // 수정 모드일 때만 삭제하기 버튼 표시
        }

        // 삭제하기 버튼 클릭 이벤트
        binding.writeDeleteBtn.setOnClickListener {
            if (key != null) {
                AlertDialog.Builder(this)
                    .setTitle("게시글 삭제")
                    .setMessage("삭제하시겠습니까?")
                    .setPositiveButton("예") { dialog, which ->
                        Firebase.database.getReference("board").child(key).removeValue()
                        Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .setNegativeButton("아니오") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

         // 2) 구글에서 firebase realtime database android 검색,
        // 2) 시작하기, 그래들에 SDK추가,
        binding.writeUploadBtn.setOnClickListener {

// Write a message to the database
            val database = Firebase.database
            val myRef = database.getReference("board")

            // 현재 날짜와 시간 포맷팅 (예: 2026.11.15 14:30)
            val currentDate = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()).format(Date())

            val newModel = Model(
                binding.writeTextArea.text.toString(),
                binding.writeContentArea.text.toString(),
                currentDate
            )

            if (key != null) {
                // 수정 모드: 해당 키 위치의 데이터를 덮어쓰기
                myRef.child(key).setValue(newModel)
            } else {
                // 새 글 작성 모드: 새로운 키를 생성하여 데이터 추가
                myRef.push().setValue(newModel)
            }
            //title 키, 값으로 저장잘 되는지 확인하고 데이터 읽기로 넘어감
            //BoardListActivity로 전환
            finish()
        }
    }
}