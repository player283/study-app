package com.example.samapp



import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.samapp.databinding.ActivityMainBinding
import com.example.samapp.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


private lateinit var auth: FirebaseAuth
private lateinit var homeFragment: HomeFragment
private lateinit var toolFragment: ToolFragment
private lateinit var groupFragment: GroupFragment
private lateinit var mypageFragment: MypageFragment
private lateinit var sellFragment: SellFragment


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val setting = binding.logoutSetting // 메인 화면의 로그아웃 기능 : 사람 icon
        setting.setOnClickListener{
            val auth = Firebase.auth
            auth.signOut()

            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)

            Toast.makeText(this,"로그아웃 되었습니다.",Toast.LENGTH_SHORT).show()
        }

        binding.bottomNav.setOnNavigationItemSelectedListener(BottomNavItemSelectedListener)

        homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragments_frame, homeFragment).commit()
    }

    private val BottomNavItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragments_frame,
                        homeFragment
                    ).commit()
                }
                R.id.menu_groups -> {
                    groupFragment = GroupFragment()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragments_frame,
                        groupFragment
                    ).commit()
                }
                R.id.menu_sell -> {
                    sellFragment = SellFragment()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragments_frame,
                        sellFragment
                    ).commit()
                }
                R.id.menu_my -> {
                    mypageFragment = MypageFragment()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragments_frame,
                        mypageFragment
                    ).commit()
                }
                R.id.menu_tool -> {
                    toolFragment = ToolFragment()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragments_frame,
                        toolFragment
                    )
                        .commit()
                }
            }
            true
        }
}