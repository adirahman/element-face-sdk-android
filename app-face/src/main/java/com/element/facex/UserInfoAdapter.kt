package com.element.facex

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.element.camera.UserInfo

class UserInfoAdapter(
    private val userInfoList: ArrayList<UserInfo>,
    private val mainActivity: FaceExMainActivity
) :
    RecyclerView.Adapter<UserInfoAdapter.MyViewHolder>() {

    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    fun refresh(updatedList: ArrayList<UserInfo>) {
        userInfoList.clear()
        userInfoList.addAll(updatedList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        textView.setTextColor(Color.BLACK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Medium)
        }
        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = userInfoList[position].name
        holder.textView.setOnClickListener {
            mainActivity.startAuth(userInfoList[position].userId)
        }
    }

    override fun getItemCount() = userInfoList.size
}