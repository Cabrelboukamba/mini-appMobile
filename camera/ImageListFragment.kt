package com.xpi.kollect.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.xpi.kollect.Adapters.ImageAdapter
import com.xpi.kollect.R
import kotlinx.android.synthetic.main.fragment_image_list.*



class ImageListFragment : Fragment(R.layout.fragment_image_list)  {

    private lateinit var recyclerView: RecyclerView
    private lateinit var imagesList : ArrayList<com.xpi.kollect.model.Collection>

    private lateinit var databaseReference: DatabaseReference
    private lateinit var adapter: ImageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = imageRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(requireContext().applicationContext)

        imagesList = arrayListOf()

        adapter = ImageAdapter(imagesList, requireContext().applicationContext)

        recyclerView.adapter = adapter

        val myQuery = FirebaseDatabase.getInstance().getReference("collections").child("images").limitToFirst(100)

        myQuery.addValueEventListener(object:ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    imagesList.clear()
                    for (dataSnapShot in snapshot.children){
                        val imageCollection = dataSnapShot.getValue(com.xpi.kollect.model.Collection::class.java)
                        imagesList.add(imageCollection!!)
                    }

                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
               Toast.makeText(requireContext().applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }

        })
    }
}