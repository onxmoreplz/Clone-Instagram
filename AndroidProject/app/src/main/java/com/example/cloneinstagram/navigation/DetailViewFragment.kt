package com.example.cloneinstagram.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloneinstagram.R
import com.example.cloneinstagram.navigation.model.ContentDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.item_detail.view.*

class DetailViewFragment :Fragment() {

    var fireStore: FirebaseFirestore?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fireStore = FirebaseFirestore.getInstance()

        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail, container, false)

        view.rv_detailviewfragment.adapter = DetailViewRecyclerViewAdapter()
        view.rv_detailviewfragment.layoutManager = LinearLayoutManager(activity)
        return view
    }

    inner class DetailViewRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var contentDTOs: ArrayList<ContentDTO> = arrayListOf()
        var contentUidList: ArrayList<String> = arrayListOf()
        init {
            fireStore?.collection("images")?.orderBy("timestamp")?.addSnapshotListener { querySnapshot, firebaseFireStoreException ->
                contentDTOs.clear()
                contentUidList.clear()
                for(snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(ContentDTO::class.java)
                    contentDTOs.add(item!!)
                    contentUidList.add(snapshot.id)
                }
                notifyDataSetChanged()
            }

        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder).itemView

            viewHolder.tv_detailviewitem_profile.text = contentDTOs!![position].userId

            //Image
            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl).into(viewHolder.iv_detailviewitem_content)

            //Explain
            viewHolder.tv_detailviewitem_explain.text = contentDTOs!![position].explain

            //likes
            viewHolder.tv_detailviewitem_favorite_counter.text = "Likes " + contentDTOs!![position].favoriteCount

            //profile Image
            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl).into(viewHolder.iv_detailviewitem_profile)
        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

    }
}