package com.element.cardex

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.element.camera.*
import com.element.camera.ElementCardScanActivity.Companion.EXTRA_DOC_TYPE_ID
import com.element.camera.ElementCardScanActivity.Companion.EXTRA_ELEMENT_TOKEN
import com.element.camera.ElementCardScanActivity.Companion.EXTRA_ELEMENT_USER_ID
import com.element.card.CardConstants.OCR_RESULT_OK
import com.element.common.PermissionUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

open class CardExMainActivity : AppCompatActivity() {

    private var myUserId: String? = null
        get() = field ?: kotlin.run {
            ElementUserUtils.getUsers(this).firstOrNull()?.userId
        }

    private var myDocTypeId: String? = null

    private var myToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)

        addUser.setSafeOnClickListener {
            EnrollFormFragment().show(supportFragmentManager, null)
        }

        localEnroll.setSafeOnClickListener {
            myUserId?.let { doLocalEnroll(it) }
        }

        docSelect.setSafeOnClickListener {
            doDocSelect()
        }

        cardScan.setSafeOnClickListener {
            myDocTypeId?.let { docTypeId ->
                doCardScan(myUserId, docTypeId)
            }
        }

        performOcr.setSafeOnClickListener {
            myToken?.let { token ->
                doPerformOcr(token)
            }
        }

        cardFaceMatch.setSafeOnClickListener {
            myUserId?.let { userId ->
                myDocTypeId?.let { docTypeId ->
                    doCardFaceMatch(userId, docTypeId)
                }
            }
        }

        ocrReview.setSafeOnClickListener {
            myUserId?.let { userId ->
                myDocTypeId?.let { docTypeId ->
                    showMessage("with ${getDocName(docTypeId)}")
                    doOcrFaceMatch(userId, docTypeId)
                }
            }
        }

        checkReviewStatus.setSafeOnClickListener {
            myUserId?.let { userId ->
                myDocTypeId?.let { docTypeId ->
                    doCheckReviewStatus(userId, docTypeId)
                }
            }
        }

        displayScannedPhotos.setSafeOnClickListener {
            myUserId?.let { userId ->
                ImageViewerFragment.viewCard(this@CardExMainActivity, userId)
            }
        }

        displaySelfies.setSafeOnClickListener {
            myUserId?.let { userId ->
                ImageViewerFragment.viewSelfie(this@CardExMainActivity, userId)
            }
        }

        deleteUser.setSafeOnClickListener {
            myUserId?.let { doDeleteUser(it) }
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
            DOC_SELECT_REQ_CODE -> {
                myDocTypeId = data?.getStringExtra(EXTRA_DOC_TYPE_ID)
            }
            CARD_REQ_CODE -> {
                myToken = data?.getStringExtra(EXTRA_ELEMENT_TOKEN)
            }
            ENROLL_REQ_CODE -> {
                if (ElementFaceSDK.isEnrolled(baseContext, myUserId)) {
                    showMessage(getString(R.string.enroll_completed))
                } else {
                    showMessage(getString(R.string.enroll_canceled))
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

    private fun updateUi() {
        if (myUserId != null) {
            userText.text = ElementUserUtils.getUser(baseContext, myUserId)?.name
            userIdTextView.text = getString(R.string.user_id_status, myUserId)
            addUser.isEnabled = false
            docSelect.isEnabled = true
            deleteUser.isEnabled = true

            if (ElementFaceSDK.isEnrolled(baseContext, myUserId)) {
                localEnroll.isEnabled = false
                displaySelfies.isEnabled = true
                cardFaceMatch.isEnabled = myToken != null
                ocrReview.isEnabled = myToken != null
            } else {
                localEnroll.isEnabled = true
                displaySelfies.isEnabled = false
                cardFaceMatch.isEnabled = false
                ocrReview.isEnabled = false
            }
        } else {
            userText.text = getString(R.string.no_user)
            userIdTextView.text = getString(R.string.user_id_status, "N/A")
            addUser.isEnabled = true
            docSelect.isEnabled = false
            localEnroll.isEnabled = false
            cardFaceMatch.isEnabled = false
            ocrReview.isEnabled = false
            ocrReview.isEnabled = false
            displaySelfies.isEnabled = false
            deleteUser.isEnabled = false
        }

        if (myDocTypeId != null) {
            docText.text = getDocName(myDocTypeId)
            docTypeIdText.text = getString(R.string.doc_type_id_status, myDocTypeId)
            cardScan.isEnabled = true
        } else {
            docText.text = getString(R.string.no_document)
            docTypeIdText.text = getString(R.string.doc_type_id_status, "N/A")
            cardScan.isEnabled = false
        }

        if (myToken != null) {
            tokenTextView.text = getString(R.string.token_status, myToken)
            performOcr.isEnabled = true
            cardFaceMatch.isEnabled = true
            ocrReview.isEnabled = true
            displayScannedPhotos.isEnabled = true
        } else {
            tokenTextView.text = getString(R.string.token_status, "N/A")
            performOcr.isEnabled = false
            displayScannedPhotos.isEnabled = false
        }

        checkReviewStatus.isEnabled = if (myUserId.isNullOrBlank() || myDocTypeId.isNullOrBlank()) {
            false
        } else {
            ElementCardSDK.hasOcrResults(baseContext, myUserId!!, myDocTypeId!!)
        }
    }

    fun refreshUi() {
        runOnUiThread { updateUi() }
    }

    private val receiver = ElementServiceResultReceiver { resultCode, resultData ->
        updateUi()
        ResultViewerFragment(resultCode, resultData).show(
            supportFragmentManager,
            ""
        )
    }

    private fun doDocSelect() {
        startActivityForResult(
            Intent(this, ElementDocSelectActivity::class.java),
            DOC_SELECT_REQ_CODE
        )
    }

    private fun doCardScan(userId: String?, docTypeId: String) {
        startActivityForResult(
            Intent(this, ElementCardScanActivity::class.java).apply {
                putExtra(EXTRA_ELEMENT_USER_ID, userId)
                putExtra(EXTRA_DOC_TYPE_ID, docTypeId)
            },
            CARD_REQ_CODE
        )
    }

    private fun doPerformOcr(token: String) {
        ElementOcrService.startCardOcrService(baseContext, receiver, token)
    }

    private fun doLocalEnroll(userId: String) {
        val intent = Intent(this@CardExMainActivity, ElementFaceEnrollActivity::class.java)
        intent.putExtra(EXTRA_ELEMENT_USER_ID, userId)
        startActivityForResult(intent, ENROLL_REQ_CODE)
    }

    private fun doCardFaceMatch(userId: String, docTypeId: String) {
        ElementOcrService.startCardToFaceMatchingService(baseContext, receiver, userId, docTypeId)
    }

    private fun doOcrFaceMatch(userId: String, docTypeId: String) {
        ElementOcrService.startUserOcrService(baseContext, receiver, userId, docTypeId)
    }

    private fun doCheckReviewStatus(userId: String, docTypeId: String) {
        ElementOcrReviewResultTask(this@CardExMainActivity, userId, docTypeId).getResult {
            ResultViewerFragment(OCR_RESULT_OK, it).show(supportFragmentManager, "")
        }
    }

    private fun doDeleteUser(userId: String) {
        ElementUserUtils.deleteUser(baseContext, userId)
        myUserId = null
        myDocTypeId = null
        myToken = null
        refreshUi()
    }
}