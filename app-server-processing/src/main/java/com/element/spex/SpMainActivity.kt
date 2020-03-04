package com.element.spex

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.element.camera.ProviderUtil
import com.element.common.PermissionUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class SpMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fabEnroll.setOnClickListener {
            val list = ProviderUtil.getUsers(baseContext, BuildConfig.APPLICATION_ID, "")
            if (list.isNotEmpty()) {
                toastMessage(getString(R.string.msg_already_enrolled, list[0].fullName))
            }

            val userDataFormFragment = ServerEnrollFormFragment()
            userDataFormFragment.setMainActivity(this@SpMainActivity)
            userDataFormFragment.show(supportFragmentManager, null)
        }

        userName.setOnClickListener {
            val list = ProviderUtil.getUsers(baseContext, BuildConfig.APPLICATION_ID, "")
            if (list.isEmpty()) {
                showMessage(getString(R.string.enroll_first))
            } else {
                startAuth(list[0].userId)
            }
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

        val list = ProviderUtil.getUsers(baseContext, BuildConfig.APPLICATION_ID, "")
        if (list.isEmpty()) {
            userName.visibility = GONE
        } else {
            userName.visibility = VISIBLE
            userName.text = list[0].fullName
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(
                findViewById(R.id.constraintLayout),
                message, Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun toastMessage(message: String) {
        runOnUiThread {
            Toast.makeText(
                    baseContext,
                    message,
                    Toast.LENGTH_LONG
            ).show()
        }
    }

    fun startEnroll(userId: String) {
        val intent = Intent(this@SpMainActivity, SpEnrollActivity::class.java)
        intent.putExtra(SpEnrollActivity.EXTRA_ELEMENT_USER_ID, userId)
        intent.putExtra(SpEnrollActivity.EXTRA_LIVENESS_DETECTION, true)
        intent.putExtra(SpEnrollActivity.EXTRA_TUTORIAL, true)
        intent.putExtra(SpEnrollActivity.EXTRA_SECONDARY_TUTORIAL, true)
        intent.putExtra(SpEnrollActivity.EXTRA_CAPTURE_MODE, "enroll")
        startActivity(intent)
    }

    fun startAuth(userId: String) {
        val intent = Intent(this@SpMainActivity, SpAuthActivity::class.java)
        intent.putExtra(SpEnrollActivity.EXTRA_ELEMENT_USER_ID, userId)
        intent.putExtra(SpEnrollActivity.EXTRA_LIVENESS_DETECTION, true)
        intent.putExtra(SpEnrollActivity.EXTRA_TUTORIAL, true)
        intent.putExtra(SpEnrollActivity.EXTRA_SECONDARY_TUTORIAL, true)
        startActivity(intent)
    }
}
