package com.quan.datn

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.quan.datn.databinding.ActivityMainBinding
import com.quan.datn.databinding.NavHeaderMainBinding
import com.quan.datn.ui.dialog.ConfirmDialog
import com.quan.datn.ui.home.HomeFragment
import com.quan.datn.ui.login.LoginActivity
import com.quan.datn.ui.profile.ProfileFragment
import com.quan.datn.ui.utils.DataManager
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var currentTag:String = ""
    private lateinit var headerBinding:NavHeaderMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        headerBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))
        headerBinding.profile = DataManager.getInfoSessionLogin(this)
        this.setTitle("Lịch sử khám bệnh")
        setSupportActionBar(binding.appBarMain.toolbar)
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBarMain.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        binding.navView.setNavigationItemSelectedListener(this)
        binding.navView.setCheckedItem(R.id.nav_home)
        currentTag = HomeFragment::class.java.name
        openFragment(
            supportFragmentManager,
            HomeFragment::class.java,
            fragmentContent = R.id.main_view
        )
    }

    fun setTitle(title:String){
        binding.appBarMain.toolbar.title = title
    }

    fun updateInfoBenhNhan(){
        headerBinding.profile = DataManager.getInfoSessionLogin(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        onHideKeyBoard()
        when (item.itemId) {
            R.id.nav_home -> {
                this.setTitle("Lịch sử khám bệnh")
                hideFragment(supportFragmentManager, currentTag)
                currentTag = HomeFragment::class.java.name
                openFragment(
                    supportFragmentManager,
                    HomeFragment::class.java,
                    fragmentContent = R.id.main_view
                )

            }

            R.id.nav_profile -> {
                this.setTitle("Thông tin cá nhân")
                hideFragment(supportFragmentManager,currentTag)
                currentTag = ProfileFragment::class.java.name
                openFragment(
                    supportFragmentManager,
                    ProfileFragment::class.java,
                    fragmentContent = R.id.main_view
                )
            }
            R.id.nav_logout -> {
                val dialog = ConfirmDialog(
                    this,
                    R.string.are_you_sure_you_want_to_logout,
                    object : ConfirmDialog.IConfirmDialog {
                        override fun onClickCancel() {
                        }

                        override fun onClickOk() {
                            FirebaseAuth.getInstance().signOut()
                            DataManager.removeLoginSession(this@MainActivity)
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.logout_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(
                                    android.R.anim.slide_out_right,
                                    android.R.anim.slide_out_right
                                )
                                this@MainActivity.finish()
                            }, 100)
                        }
                    })
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                return super.onOptionsItemSelected(item)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    companion object {

        @JvmStatic
        fun openFragment(
            manager: FragmentManager,
            clazz: Class<out Fragment>,
            fragmentContent: Int,
            data:Any ?= null
        ): Fragment? {
            val tag = clazz.name
            var fragment: Fragment?
            val transaction = manager.beginTransaction()
            try {
                fragment = manager.findFragmentByTag(tag)
                if (data != null) {
                    val bundle = Bundle()
                    bundle.putString("DATA_SENDER", Gson().toJson(data))
                    fragment?.arguments = bundle
                }
                if (fragment == null || !fragment.isAdded) {
                    fragment = clazz.newInstance()
                    transaction.add(fragmentContent, fragment, tag)
                } else {
                    transaction.show(fragment)
                }
                transaction.commit()
                return fragment
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            return null
        }

        @JvmStatic
        fun hideFragment(
            manager: FragmentManager,
            tag: String
        ) {
            val fragment = manager.findFragmentByTag(tag)
            val transaction = manager.beginTransaction()
            if (fragment != null) {
                if (fragment.isVisible) {
                    transaction.hide(fragment)
                    transaction.commit()
                }
            }
        }

    }

    fun onHideKeyBoard() : Boolean {
        val view = currentFocus ?: return false
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}