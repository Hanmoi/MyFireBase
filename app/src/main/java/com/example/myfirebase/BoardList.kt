package com.example.myfirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myfirebase.databinding.ActivityBoardListBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class BoardList : AppCompatActivity() {
    lateinit var LVAdapter : ListViewAdapter
    val list = mutableListOf<Model>()
    val keyList = mutableListOf<String>() // 파이어베이스 데이터 삭제를 위한 고유 키 목록

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityBoardListBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.writeBtn.setOnClickListener { //글쓰기 버튼을 누르면 글쓰기화면으로 전환
            val intent = Intent(this, BoardWriteActivity::class.java)
            startActivity(intent)
        }
        //리스트뷰 만든 후 코딩

     //   list.add(Model("A"))
     //   list.add(Model("B"))
     //   list.add(Model("C")) 리스트뷰에 A,B,C가 잘 찍히나 확인 후 주석
        LVAdapter = ListViewAdapter(list)
        val lv = findViewById<ListView>(R.id.lv)
        lv.adapter = LVAdapter

        // 리스트뷰 아이템을 짧게 클릭했을 때 수정 화면으로 이동
        lv.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, BoardWriteActivity::class.java)
            intent.putExtra("key", keyList[position])
            intent.putExtra("title", list[position].title)
            intent.putExtra("content", list[position].content)
            startActivity(intent)
        }

        // 리스트뷰 아이템을 길게 눌렀을 때 삭제 확인 다이얼로그 띄우기
        lv.setOnItemLongClickListener { parent, view, position, id ->
            AlertDialog.Builder(this)
                .setTitle("게시글 삭제")
                .setMessage("삭제하시겠습니까?")
                .setPositiveButton("예") { dialog, which ->
                    val key = keyList[position]
                    Firebase.database.getReference("board").child(key).removeValue()
                    Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("아니오") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
            true
        }

        getData()
    }
    fun getData() //파이어베이스에서 데이터를 받아옴
    {
//BoardWrite에서 복사
        val database = Firebase.database
        val myRef = database.getReference("board")


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
          //      val post = dataSnapshot.getValue<Post>()
             //   Log.d("BoardList", dataSnapshot.toString()) //데이터 찍어보기
                // ...
                list.clear()
                keyList.clear()
                for(dataModel in dataSnapshot.children)
                {
                  //  Log.d("BoardList", dataModel.toString()) //데이터 찍어보기
                    val item = dataModel.getValue(Model::class.java)
                    Log.d("BoardList", item.toString()) //데이터 찍어보기
                    //리스트뷰 만들기 activity_board_list
                    //리스트뷰 완성후
                    if (item != null) {
                        list.add(item)
                        keyList.add(dataModel.key.toString())
                    }
                }
                //
                LVAdapter.notifyDataSetChanged() //데이터 동기화를 시켜줘야 getData해서 리스트뷰에 찍힘
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("BoardList", "loadPost:onCancelled",
                    databaseError.toException())
            }
        }
     //   postReference.addValueEventListener(postListener)
        myRef.addValueEventListener(postListener)
    }
}