package com.example.samapp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.samapp.GroupMessageActivity
import com.example.samapp.GroupRegActivity
import com.example.samapp.R
import com.example.samapp.databinding.FragmentGroupBinding
import com.example.samapp.model.GroupChatModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class GroupFragment : Fragment() {
    private var groupChatList : ArrayList<GroupChatModel> = ArrayList<GroupChatModel>()
    private var recyclerView: RecyclerView? = null
    lateinit var binding : FragmentGroupBinding
    companion object{
        fun newInstance() : GroupFragment {
            return GroupFragment()
        }
    }


    private lateinit var database: DatabaseReference


    private var uid : String? = null

    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onStart() {
        super.onStart()
        recyclerView = binding.groupListRecycler
        val manager = LinearLayoutManager(this.context)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        recyclerView?.layoutManager=manager
    }
    //프레그먼트를 포함하고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    init {
        // groupChatList에 추가
        FirebaseDatabase.getInstance().reference.child("groupChatrooms").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("init 부분","success....?")
                groupChatList.clear()
                for(data in snapshot.children){
                    val item = data.getValue<GroupChatModel>()
                    Log.d("init 부분","${data.value}")
                    Log.d("init 부분","${item}")

                    groupChatList.add(item!!)
                }

                Log.d("init 부분","${groupChatList}")
                //this는 액티비티에서 사용가능, 프래그먼트는 requireContext()로 context 가져오기
                val recyclerView = view?.findViewById<RecyclerView>(R.id.groupList_recycler)
                recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                recyclerView?.adapter = RecyclerViewAdapter()
            }
        })
    }

    //뷰가 생성되었을 때
    //프레그먼트와 레이아웃을 연결시켜주는 부분
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        database = Firebase.database.reference
        binding= FragmentGroupBinding.inflate(layoutInflater, container, false)

//            val view = inflater.inflate(com.google.firebase.database.R.layout.fragment_group_list, container, false)


        // 영어 모임 추가 버튼을 눌러 모임 생성 페이지로 이동
//        val regGroup = view?.findViewById<Button>(com.google.firebase.database.R.id.btnReg_grouplist)
       binding.btnRegGrouplist.setOnClickListener {
            val groupIntent = Intent(context, GroupRegActivity::class.java)
            context?.startActivity(groupIntent)
        }

        return binding.root
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            return CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.item_goup_list, parent, false))
        }

        inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.home_item_iv)
            val textView : TextView = itemView.findViewById(R.id.home_item_tv)
            val textViewEmail : TextView = itemView.findViewById(R.id.home_item_email)
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            // 작성된 리스트를 가져와 순서대로 출력(역순으로 고칠 것)
            Log.d("adapter 내부","$groupChatList")
            Glide.with(holder.itemView.context).load(R.drawable.file_case)
                .apply(RequestOptions().circleCrop())
                .override(100,100)
                .into(holder.imageView)
            holder.textView.text = groupChatList[position].groupName
            holder.textViewEmail.text = groupChatList[position].groupDes

            // 아이템을 클릭하면 해당 채팅방으로 이동(uid 비교하여 users에 추가)
            holder.itemView.setOnClickListener{
                uid = Firebase.auth.currentUser?.uid.toString()
                var gid = groupChatList[position].groupId
                var chief = groupChatList[position].chief
                Log.d("인원수","${groupChatList[position].userLimit} / ${groupChatList[position].users.size}")

                val intent = Intent(context, GroupMessageActivity::class.java)
                if(groupChatList[position].users.contains(uid)){
                    intent.putExtra("gId",gid)
                    context?.startActivity(intent)
                }else if(!groupChatList[position].users.contains(uid) && groupChatList[position].userLimit > groupChatList[position].users.size){
                    groupChatList[position].users.put(uid!!,true)
                    FirebaseDatabase.getInstance().getReference("groupChatrooms").child("$gid/users").setValue(groupChatList[position].users)
                    intent.putExtra("gId",gid)
                    intent.putExtra("chief",chief)
                    context?.startActivity(intent)
                }else if(groupChatList[position].userLimit <= groupChatList[position].users.size){
                    Toast.makeText(context,"모임의 인원이 가득찼습니다.", Toast.LENGTH_SHORT).show()
                }

            }
        }

        override fun getItemCount(): Int {
            return groupChatList.size
            notifyDataSetChanged()
            Log.d("getItemCount","${groupChatList.size}")
        }

    }

}