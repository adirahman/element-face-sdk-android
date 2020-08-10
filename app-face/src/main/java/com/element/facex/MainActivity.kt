package com.element.facex

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.element.camera.*
import com.element.common.PermissionUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

open class MainActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        val ENROLL_REQ_CODE = 12800

        @JvmStatic
        val AUTH_REQ_CODE = 12801
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fabEnroll.setOnClickListener {
            val mobileEnrollFormFragment = EnrollFormFragment()
            mobileEnrollFormFragment.show(supportFragmentManager, null)
        }

        fabFetch.setOnClickListener {
            val featureFormFragment = FetchFormFragment()
            featureFormFragment.show(supportFragmentManager, null)
        }

        userList.layoutManager = LinearLayoutManager(baseContext)
        userList.adapter = UserInfoAdapter(ArrayList(ElementUserUtils.getUsers(baseContext)), this@MainActivity)

        val itemDecor = DividerItemDecoration(baseContext, VERTICAL)
        userList.addItemDecoration(itemDecor)
    }

    fun onFeaturesFetched() {
        runOnUiThread {
            (userList.adapter as UserInfoAdapter).refresh(ArrayList(ElementUserUtils.getUsers(baseContext)))
        }
    }

    override fun onResume() {
        super.onResume()
        PermissionUtils.verifyPermissions(
                this,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        )

        (userList.adapter as UserInfoAdapter).refresh(ArrayList(ElementUserUtils.getUsers(baseContext)))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(baseContext, SettingsActivity::class.java))
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ENROLL_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                showMessage(getString(R.string.enroll_completed))
            } else {
                showMessage(getString(R.string.enroll_cancelled))
            }
        } else if (requestCode == AUTH_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val userId = data?.getStringExtra(ElementFaceAuthActivity.EXTRA_ELEMENT_USER_ID)
                val userInfo = ElementUserUtils.getUser(baseContext, userId!!)
                val results = data.getStringExtra(ElementFaceAuthActivity.EXTRA_RESULTS)
                val message: String
                message = when (results) {
                    ElementFaceAuthActivity.USER_VERIFIED -> getString(
                            R.string.msg_verified,
                            userInfo.name
                    )
                    ElementFaceAuthActivity.USER_FAKE -> getString(R.string.msg_fake)
                    else -> getString(R.string.msg_not_verified)
                }
                showMessage(message)
            } else {
                showMessage(getString(R.string.auth_cancelled))
            }
        }
    }

    private fun showMessage(message: String) {
        runOnUiThread {
            Toast.makeText(
                    baseContext,
                    message,
                    Toast.LENGTH_LONG
            ).show()
        }
    }

    fun startFetch(userId: String) {
        val url = getString(R.string.query_api_url)
        val callback = object : ElementUserQueryTask.Callback {
            override fun onComplete(p0: UserInfo?, p1: Boolean) {
                onFeaturesFetched()
                showMessage(getString(R.string.msg_fetching_succeed))
            }

            override fun onError(p0: Int, p1: String, p2: MutableMap<String, Any>) {
                showMessage(getString(R.string.msg_fetching_failed))
            }
        }

        ElementUserQueryTask(baseContext, url, userId)
                .post(callback)
    }

    fun startAuth(userId: String) {
        val uiDelegate = PreferenceManager.getDefaultSharedPreferences(baseContext).getString("uiDelegate", "SelfieDot")
        val processing = PreferenceManager.getDefaultSharedPreferences(baseContext).getString("processing", "Local")
        if ("Local" == processing) {
            localAuth(userId, uiDelegate)
        } else {
            serverSideAuth(userId, uiDelegate)
        }
    }

    private fun localAuth(userId: String, uiDelegate: String?) {
        val intent = Intent(this@MainActivity, ElementFaceAuthActivity::class.java)
        intent.putExtra(ElementFaceAuthActivity.EXTRA_ELEMENT_USER_ID, userId)
        intent.putExtra(ElementFaceAuthActivity.EXTRA_UI_DELEGATE, uiDelegate)
        intent.putExtra(ElementFaceEnrollActivity.EXTRA_TUTORIAL, true)
        intent.putExtra(ElementFaceEnrollActivity.EXTRA_SECONDARY_TUTORIAL, true)
        startActivityForResult(intent, AUTH_REQ_CODE)
    }

    private fun serverSideAuth(userId: String, uiDelegate: String?) {
        val intent = Intent(this@MainActivity, ServerAuthActivity::class.java)
        intent.putExtra(ServerEnrollActivity.EXTRA_ELEMENT_USER_ID, userId)
        intent.putExtra(ElementFaceAuthActivity.EXTRA_UI_DELEGATE, uiDelegate)
        intent.putExtra(ServerEnrollActivity.EXTRA_TUTORIAL, true)
        intent.putExtra(ServerEnrollActivity.EXTRA_SECONDARY_TUTORIAL, true)
        startActivity(intent)
    }

    fun startEnroll(userId: String) {
        val uiDelegate = PreferenceManager.getDefaultSharedPreferences(baseContext).getString("uiDelegate", "SelfieDot")
        val processing = PreferenceManager.getDefaultSharedPreferences(baseContext).getString("processing", "Local")
        if ("Local" == processing) {
            localEnroll(userId, uiDelegate)
        } else {
            serverSideEnroll(userId, uiDelegate)
        }
    }

    private fun localEnroll(userId: String, uiDelegate: String?) {
        val intent = Intent(this@MainActivity, ElementFaceEnrollActivity::class.java)
        intent.putExtra(ElementFaceEnrollActivity.EXTRA_ELEMENT_USER_ID, userId)
        intent.putExtra(ElementFaceAuthActivity.EXTRA_UI_DELEGATE, uiDelegate)
        intent.putExtra(ElementFaceEnrollActivity.EXTRA_TUTORIAL, true)
        intent.putExtra(ElementFaceEnrollActivity.EXTRA_SECONDARY_TUTORIAL, true)
        startActivityForResult(intent, ENROLL_REQ_CODE)
    }

    private fun serverSideEnroll(userId: String, uiDelegate: String?) {
        val intent = Intent(this@MainActivity, ServerEnrollActivity::class.java)
        intent.putExtra(ServerEnrollActivity.EXTRA_ELEMENT_USER_ID, userId)
        intent.putExtra(ElementFaceAuthActivity.EXTRA_UI_DELEGATE, uiDelegate)
        intent.putExtra(ServerEnrollActivity.EXTRA_TUTORIAL, true)
        intent.putExtra(ServerEnrollActivity.EXTRA_SECONDARY_TUTORIAL, true)
        intent.putExtra(ServerEnrollActivity.EXTRA_CAPTURE_MODE, "enroll")
        startActivity(intent)
    }
}
