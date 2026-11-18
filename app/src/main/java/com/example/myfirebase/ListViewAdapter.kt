package com.example.myfirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

//Alt+Enter로 재정의
class ListViewAdapter(val List: MutableList<Model>): BaseAdapter() {
    override fun getCount(): Int { //갖고 있는 데이터 목록의 전체 개수를 알려줍니다.
      return List.count()
    }

    override fun getItem(position: Int): Any? { //특정 위치에 들어갈 실제 데이터 항목을 가져옵니다.
       return List[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView( //XML 레이아웃을 불러와서 텍스트나 이미지를 바인딩(넣기)한 후 완성된 View를 반환합니다.
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var view = convertView
        if(view == null)
        {
view = LayoutInflater.from(parent?.context).inflate(R.layout.listview_item, parent, false)
        }

        val title = view?.findViewById<TextView>(R.id.itemTextId)
        val content = view?.findViewById<TextView>(R.id.itemContentId)
        val date = view?.findViewById<TextView>(R.id.itemDateId)
        title!!.text = List[position].title
        content!!.text = List[position].content
        date!!.text = List[position].date
        return view
        //ListActivity로 가서 Adapter 선언하기
    }
}