package com.element.cardex

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.element.camera.*
import com.element.camera.ElementCardScanActivity.Companion.EXTRA_DOC_TYPE_ID
import com.element.camera.ElementCardScanActivity.Companion.EXTRA_ELEMENT_TOKEN
import com.element.camera.ElementCardScanActivity.Companion.EXTRA_ELEMENT_USER_ID
import com.element.card.BuildConfig
import com.element.card.CardConstants.OCR_RESULT_OK
import com.element.common.PermissionUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

open class MainActivity : AppCompatActivity() {

    private var myUserId: String? = null
        get() = field ?: kotlin.run {
            ElementUserUtils.getUsers(this).firstOrNull()?.userId
        }

    private var myDocTypeId: String = ElementCardSDK.getDocs().first().first

    private var myToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME
        setSupportActionBar(toolbar)

        selectDoc.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                ElementCardSDK.getDocs().map { it.second })
        selectDoc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                myDocTypeId = ElementCardSDK.getDocs().first { it.second == parent?.getItemAtPosition(position) }.first
                myToken = ElementCardSDK.getUserToken(this@MainActivity, myUserId, myDocTypeId)
                refreshUi()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        selectDoc.setSelection(0)

        scanCard.setOnClickListener {
            startCardScan(myUserId, myDocTypeId)
        }

        pickPhoto.setOnClickListener {
            startCardPicker(myDocTypeId)
        }

        ocrCardScan.setOnClickListener {
            myToken?.let { startCardOcrService(it) }
        }

        displayPhoto.setOnClickListener {
            myToken?.let { startDisplayPhotos(it) }
        }

        assignCardTokens.setOnClickListener {
            myUserId?.let { userId ->
                myToken?.let { token ->
                    startAssignToken(userId, token)
                }
            }
        }

        addUser.setOnClickListener {
            startAddUser()
        }

        ocrFm.setOnClickListener {
            myUserId?.let { userId ->
                showMessage("with ${getDocName(myDocTypeId)}")
                startOcrFm(userId, myDocTypeId)
            }
        }

        localEnroll.setOnClickListener {
            myUserId?.let { startLocalEnroll(it) }
        }

        ocrMatching.setOnClickListener {
            myUserId?.let { userId ->
                showMessage("with ${getDocName(myDocTypeId)}")
                startOcrMatching(userId, myDocTypeId)
            }
        }

        ocrStatus.setOnClickListener {
            myUserId?.let { startCheckReviewStatus(it, myDocTypeId) }
        }

        deleteUser.setOnClickListener {
            myUserId?.let { startDelete(it) }
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
        refreshUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CARD_REQ_CODE -> {
                myToken = data?.getStringExtra(EXTRA_ELEMENT_TOKEN)
                if (resultCode == Activity.RESULT_OK) {
                    showMessage(getString(R.string.card_completed))
                } else {
                    showMessage(getString(R.string.card_cancelled))
                }
            }
            ENROLL_REQ_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    showMessage(getString(R.string.enroll_completed))
                } else {
                    showMessage(getString(R.string.enroll_cancelled))
                }
            }
            AUTH_REQ_CODE -> {
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
        refreshUi()
    }

    private fun showMessage(message: String) {
        runOnUiThread {
            Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun initUi() {
        docTokenTextView.text = getString(R.string.token_status, "N/A")
        userNameTextView.text = myUserId?.getUserName(baseContext) ?: getString(R.string.no_user)
        userIdTextView.text = getString(R.string.user_id_status, "N/A")
        userDocTextView.text = getString(R.string.user_doc_status, "N/A")

        addUser.isEnabled = true

        displayPhoto.isEnabled = false
        ocrCardScan.isEnabled = false
        assignCardTokens.isEnabled = false
        ocrFm.isEnabled = false
        localEnroll.isEnabled = false
        ocrMatching.isEnabled = false
        deleteUser.isEnabled = false
    }

    private fun updateUi() {
        initUi()

        if (ElementCardSDK.validateToken(myToken)) {
            docTokenTextView.text = getString(R.string.token_status, myToken)
            ocrCardScan.isEnabled = true
            displayPhoto.isEnabled = true
            assignCardTokens.isEnabled = myUserId != null
        }

        if (myUserId != null) {
            val isEnrolled = ElementFaceSDK.isEnrolled(baseContext, myUserId)
            userIdTextView.text = getString(R.string.user_id_status, myUserId)
            localEnroll.isEnabled = !isEnrolled
            addUser.isEnabled = false
            deleteUser.isEnabled = true

            getUserScannedDocs(baseContext, myUserId).run {
                when (size) {
                    0 -> {
                        userDocTextView.text = getString(R.string.user_doc_status, "N/A")
                    }
                    1 -> {
                        userDocTextView.text = getString(R.string.user_doc_status, first().second)
                        ocrMatching.isEnabled = isEnrolled
                        ocrFm.isEnabled = !isEnrolled
                    }
                    else -> {
                        val formatted = "(${size}) ${joinToString(", ") { it.second }}"
                        userDocTextView.text = getString(R.string.user_doc_status, formatted)
                        ocrMatching.isEnabled = isEnrolled
                        ocrFm.isEnabled = !isEnrolled
                    }
                }
            }
        }
        ocrStatus.isEnabled = ElementCardSDK.hasOcrResults(baseContext, myUserId, myDocTypeId)
    }

    fun refreshUi() {
        runOnUiThread { updateUi() }
    }

    private val receiver = ElementServiceResultReceiver { resultCode, resultData ->
        updateUi()
        ResultViewerFragment(resultCode, resultData, myUserId, myDocTypeId).show(supportFragmentManager, "")
    }

    private fun startCardPicker(docTypeId: String) {
        CardPickerFragment(docTypeId) { cardPhotos ->
            myToken = ElementCardSDK.applyForToken(docTypeId, cardPhotos)
            myUserId?.let { userId ->
                myToken?.let { token ->
                    startAssignToken(userId, token)
                }
            }
            refreshUi()
        }.show(supportFragmentManager, null)
    }

    private fun startCardScan(userId: String?, docTypeId: String) {
        val intent = Intent(this, ElementCardScanActivity::class.java).apply {
            putExtra(EXTRA_ELEMENT_USER_ID, userId)
            putExtra(EXTRA_DOC_TYPE_ID, docTypeId)
        }
        startActivityForResult(intent, CARD_REQ_CODE)
    }

    private fun startCardOcrService(token: String) {
        ElementOcrService.startCardOcrService(baseContext, receiver, token)
    }

    private fun startAssignToken(userId: String, token: String) {
        ElementCardSDK.assignTokenToUser(baseContext, userId, token)
        refreshUi()
    }

    private fun startDisplayPhotos(token: String) {
        ImageViewerFragment(token).show(supportFragmentManager, null)
    }

    private fun startAddUser() {
        EnrollFormFragment().show(supportFragmentManager, null)
    }

    private fun startOcrFm(userId: String, docTypeId: String) {
        val intent = Intent(this@MainActivity, CardToFaceMatchingActivity::class.java)
        intent.putExtra(EXTRA_ELEMENT_USER_ID, userId)
        intent.putExtra(EXTRA_DOC_TYPE_ID, docTypeId)
        startActivity(intent)
    }

    private fun startLocalEnroll(userId: String) {
        val intent = Intent(this@MainActivity, ElementFaceEnrollActivity::class.java)
        intent.putExtra(EXTRA_ELEMENT_USER_ID, userId)
        startActivityForResult(intent, ENROLL_REQ_CODE)
    }

    private fun startOcrMatching(userId: String, docTypeId: String) {
        ElementOcrService.startUserOcrService(baseContext, receiver, userId, docTypeId)
    }

    private fun startCheckReviewStatus(userId: String, docTypeId: String) {
        ElementOcrReviewResultTask(this@MainActivity, userId, docTypeId).getResult {
            ResultViewerFragment(OCR_RESULT_OK, it).show(supportFragmentManager, "")
        }
    }

    private fun startDelete(userId: String) {
        ElementUserUtils.deleteUser(baseContext, userId)
        refreshUi()
    }
}