package com.example.myfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myfirebase.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    //1.
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize Firebase Auth
        //1.
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        //뷰바인딩
        val binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
    //    setContentView(R.layout.activity_main)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //2. 앱을 재시작하면 마지막 회원가입했던 UID가 출력
        Toast.makeText(this, auth.currentUser?.uid.toString(),
            Toast.LENGTH_SHORT).show()


        binding.joinBtn.setOnClickListener {
            val email = binding.emailArea.text.toString().trim()
            val pwd = binding.pwdArea.text.toString().trim()
            if (email.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

     // 1.    auth.createUserWithEmailAndPassword("abc@abc.com", "12341234")
            auth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "no",Toast.LENGTH_SHORT).show()
                    }
                }
        }
//3. 로그아웃
        binding.logoutBtn.setOnClickListener { //로그아웃 누르면 로그아웃
            auth.signOut()
            Toast.makeText(this, auth.currentUser?.uid.toString(),
                Toast.LENGTH_SHORT).show()
        }
//4. 로그인
        binding.logInBtn.setOnClickListener {
            val email = binding.emailArea.text.toString().trim()
            val pwd = binding.pwdArea.text.toString().trim()
            if (email.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) { //로그인 됐을 때
                      //  Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, auth.currentUser?.uid.toString(), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, BoardList::class.java)
                        startActivity(intent)
                    } else { //안됐을 때
                        Toast.makeText(this, "no", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}