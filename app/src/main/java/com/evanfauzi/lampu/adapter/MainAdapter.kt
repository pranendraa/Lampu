package com.evanfauzi.lampu.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.evanfauzi.lampu.R
import com.evanfauzi.lampu.holder.MainViewHolder
import com.evanfauzi.lampu.model.ModelLampu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*

class MainAdapter(private val context: Context,
                  private val daftarModelLampu: ArrayList<ModelLampu?>?,
private val mDatabaseReference: DatabaseReference) : RecyclerView.Adapter<MainViewHolder>() {
    private val listener: FirebaseDataListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
//        holder.view.setBackgroundColor(#f0f4f4)
        val lmp = daftarModelLampu?.get(position)?.key
        Log.d("TAG", "ob: "+lmp)
        mDatabaseReference.child("lampu").child(lmp!!).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lampu: ModelLampu = snapshot.getValue(ModelLampu::class.java)!!
                val status = lampu.nilai
                Log.d("TAG", "onBindViewHolder: "+snapshot.getValue())
                if(status == 1){
                    holder.tombol.isChecked = false
                }else if (status == 0){
                    holder.tombol.isChecked = true
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        holder.no.text = (position+1).toString()
//        holder.tombol.setOnClickListener { listener.onDataClick(daftarModelLampu?.get(position), position) }
        holder.tombol.setOnClickListener { listener.onItemClicked(daftarModelLampu?.get(position), position)}
//        holder.view.setOnClickListener { listener.onDataClick(daftarModelLampu?.get(position), position) }
    }

    override fun getItemCount(): Int {
        return daftarModelLampu?.size!!
    }

    //interface data listener
    interface FirebaseDataListener {
//        fun onDataClick(lampu: ModelLampu?, position: Int)
        fun onItemClicked(lampu: ModelLampu?, position: Int)
    }

    init {
        listener = context as FirebaseDataListener

    }
}