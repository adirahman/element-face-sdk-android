package com.element.facex

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
import com.element.camera.ProcessResultCode.*
import com.element.common.PermissionUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

open class FaceExMainActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        val ENROLL_REQ_CODE = 12800

        @JvmStatic
        val AUTH_REQ_CODE = 12801
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT)
        setSupportActionBar(toolbar)

        fabEnroll.setOnClickListener {
            EnrollFormFragment().show(supportFragmentManager, null)
        }
        fabFetch.setOnClickListener {
            FetchFormFragment().show(supportFragmentManager, null)
        }

        facexUserList.layoutManager = LinearLayoutManager(baseContext)
        facexUserList.adapter =
            UserInfoAdapter(
                ArrayList(ElementUserUtils.getUsers(baseContext)),
                this@FaceExMainActivity
            )

        facexUserList.addItemDecoration(DividerItemDecoration(baseContext, VERTICAL))
    }

    fun onFeaturesFetched() {
        runOnUiThread {
            (facexUserList.adapter as UserInfoAdapter).refresh(
                ArrayList(
                    ElementUserUtils.getUsers(
                        baseContext
                    )
                )
            )
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
        (facexUserList.adapter as UserInfoAdapter).refresh(
            ArrayList(
                ElementUserUtils.getUsers(
                    baseContext
                )
            )
        )
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

        val userId = data?.getStringExtra(ElementFaceAuthActivity.EXTRA_ELEMENT_USER_ID)
        val cancelMessage =
            data?.getStringExtra(ElementFaceAuthActivity.RESULT_EXTRA_CANCEL_MESSAGES)
        when (requestCode) {
            ENROLL_REQ_CODE -> {
                if (resultCode == RESULT_OK) {
                    val isUserEnrolled = ElementFaceSDK.isEnrolled(baseContext, userId)
                    if (isUserEnrolled) {
                        showResult(getString(R.string.enroll_completed))
                    } else {
                        showResult(getString(R.string.enroll_cancelled))
                    }
                } else {
                    showResult(cancelMessage ?: getString(R.string.enroll_cancelled))
                }
            }
            AUTH_REQ_CODE -> {
                if (resultCode == RESULT_OK) {
                    val userInfo = ElementUserUtils.getUser(baseContext, userId!!)
                    ElementFaceSDK.getRecentAuthenticationResult(baseContext, userId)?.also {
                        val message: String = when (it.code) {
                            VERIFIED -> getString(R.string.auth_verified, userInfo.name)
                            NOT_VERIFIED -> getString(R.string.auth_not_verified, it.falseAttempts)
                            ABORTED -> getString(R.string.auth_aborted, it.falseAttempts)
                            else -> getString(R.string.auth_failed, it.falseAttempts)
                        }
                        showResult(message)
                    } ?: showResult(cancelMessage ?: getString(R.string.auth_canceled))
                } else {
                    showResult(cancelMessage ?: getString(R.string.auth_canceled))
                }
            }
        }
    }

    private fun showResult(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(R.string.button_ok) { _, _ -> }
            .show()
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
        ElementUserQueryTask(baseContext, url, userId)
            .post(object : ElementUserQueryTask.Callback {
                override fun onComplete(p0: UserInfo?, p1: Boolean) {
                    onFeaturesFetched()
                    showMessage(getString(R.string.msg_fetching_succeed))
                }

                override fun onError(p0: Int, p1: String, p2: MutableMap<String, Any>) {
                    showMessage(getString(R.string.msg_fetching_failed))
                }
            })
    }

    open fun startEnroll(userId: String) {
        val uiDelegate = PreferenceManager.getDefaultSharedPreferences(baseContext)
            .getString("uiDelegate", "SelfieDotV2")
        val processing = PreferenceManager.getDefaultSharedPreferences(baseContext)
            .getString("processing", "Local")
        if ("Server Side" == processing) {
            serverSideEnroll(userId, uiDelegate)
        } else {
            localEnroll(userId, uiDelegate)
        }
    }

    private fun localEnroll(userId: String, uiDelegate: String?) {
        val intent = Intent(this@FaceExMainActivity, ElementFaceEnrollActivity::class.java).apply {
            putExtra(ElementFaceEnrollActivity.EXTRA_ELEMENT_USER_ID, userId)
            putExtra(ElementFaceEnrollActivity.EXTRA_UI_DELEGATE, uiDelegate)
        }
        startActivityForResult(intent, ENROLL_REQ_CODE)
    }

    private fun serverSideEnroll(userId: String, uiDelegate: String?) {
        val intent = Intent(this@FaceExMainActivity, ServerSideEnrollActivity::class.java).apply {
            putExtra(ElementFaceCaptureActivity.EXTRA_ELEMENT_USER_ID, userId)
            putExtra(ElementFaceCaptureActivity.EXTRA_UI_DELEGATE, uiDelegate)
            putExtra(ElementFaceCaptureActivity.EXTRA_CAPTURE_MODE, "enroll")
        }
        startActivity(intent)
    }

    open fun startAuth(userId: String) {
        val uiDelegate = PreferenceManager.getDefaultSharedPreferences(baseContext)
            .getString("uiDelegate", "SelfieDotV2")
        val processing = PreferenceManager.getDefaultSharedPreferences(baseContext)
            .getString("processing", "Local")
        if ("Server Side" == processing) {
            serverSideAuth(userId, uiDelegate)
        } else {
            localAuth(userId, uiDelegate)
        }
    }

    private fun localAuth(userId: String, uiDelegate: String?) {
        val clazz = if (uiDelegate == "SelfieDotV2") {
            LocalAuthTransparentActivity::class.java
        } else {
            LocalAuthActivity::class.java
        }

        val intent = Intent(this@FaceExMainActivity, clazz).apply {
            putExtra(ElementFaceAuthActivity.EXTRA_ELEMENT_USER_ID, userId)
            putExtra(ElementFaceAuthActivity.EXTRA_UI_DELEGATE, uiDelegate)
        }
        startActivityForResult(intent, AUTH_REQ_CODE)
    }

    private fun serverSideAuth(userId: String, uiDelegate: String?) {
        val clazz = if (uiDelegate == "SelfieDotV2") {
            ServerSideAuthTransparentActivity::class.java
        } else {
            ServerSideAuthActivity::class.java
        }

        val intent = Intent(this@FaceExMainActivity, clazz).apply {
            putExtra(ElementFaceCaptureActivity.EXTRA_ELEMENT_USER_ID, userId)
            putExtra(ElementFaceCaptureActivity.EXTRA_UI_DELEGATE, uiDelegate)
        }
        startActivity(intent)
    }
}
