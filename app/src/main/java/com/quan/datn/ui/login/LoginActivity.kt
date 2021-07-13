package com.quan.datn.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.quan.datn.MainActivity
import com.quan.datn.R
import com.quan.datn.databinding.ActivityLoginBinding
import com.quan.datn.ui.splash.SplashViewModel
import com.quan.datn.ui.utils.DataManager
import com.quan.datn.ui.utils.StringUtils.isNotNullOrEmpty
import com.quan.datn.ui.utils.StringUtils.isPhoneNumber
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity(), LoginCallBack {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var storedVerificationId: String = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var isSendSms = false
    private var isSendOTPSuccess = false
    private var timeOut: CountDownTimer? = null
    private lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.callBack = this
        auth = FirebaseAuth.getInstance()
        auth.languageCode = "vi"
        setupUI()
        setupEvent()
    }

    private fun setupUI() {
        binding.btnLogin.text = getString(R.string.receive_otp)
    }

    private fun setupEvent() {
        binding.btnLogin.setOnClickListener {
            binding.txtSdt.text.toString().isPhoneNumber { phoneNumber ->
                if (phoneNumber != null) {
                    binding.processLoading.visibility = View.VISIBLE
                    if (isSendOTPSuccess && binding.txtOtp.isVisible) {
                        val otp = binding.txtOtp.text.toString()
                        if (otp.isNotBlank() && otp.length == 6) {
                            val credential: PhoneAuthCredential =
                                PhoneAuthProvider.getCredential(storedVerificationId, otp)
                            verifyCode(credential)
                        } else {
                            showMessage(getString(R.string.otp_not_black))
                            binding.txtOtp.error = getString(R.string.otp_not_black)
                        }
                    } else {
                        viewModel.checkPhoneNumber(phoneNumber)
                    }
                } else {
                    binding.processLoading.visibility = View.GONE
                    showMessage(getString(R.string.phonenumber_is_not_format))
                }
            }
        }

        //=============== Lang nghe su kien gui ma xac thuc OTP =========
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                credential.smsCode.let {
                    binding.txtOtp.setText(it)
                    verifyCode(credential)
                    timeOut?.cancel()
                    timeOut?.onFinish()
                }
            }

            // Gui ma xac nhan bi loi
            override fun onVerificationFailed(e: FirebaseException) {
                showMessage("${getString(R.string.failed_to_send_sms_code)}\n ${e.message}")
                isSendSms = false
                isSendOTPSuccess = false
                binding.processLoading.visibility = View.GONE
                e.printStackTrace()
            }

            // Gui ma xac nhan thanh cong
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                showMessage(getString(R.string.send_sms_code_success))
                binding.processLoading.visibility = View.GONE
                storedVerificationId = verificationId
                resendToken = token
                isSendSms = true
                isSendOTPSuccess = true
                countTimeOutSms()
                binding.textInputOTP.visibility = View.VISIBLE
                binding.btnLogin.text = getString(R.string.login)
            }
        }
    }

    //=============== Sau khi xac thuc ma OTP thanh cong =========
    private fun verifyOTPSuccess() {
        binding.processLoading.visibility = View.GONE
        timeOut?.cancel()
        timeOut?.onFinish()
        viewModel.getInfoBenhNhan(this)
    }

    private fun sendSMSCode(phoneNumber: String) {
        binding.processLoading.visibility = View.VISIBLE

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resendSMSCode(phoneNumber: String) {
        binding.processLoading.visibility = View.VISIBLE
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyCode(credential: PhoneAuthCredential) {
        binding.processLoading.visibility = View.VISIBLE
        auth.signInWithCredential(credential).addOnCompleteListener {
            binding.processLoading.visibility = View.GONE
            if (it.isSuccessful) {
                runOnUiThread {
                    verifyOTPSuccess()
                }
            } else {
                if (it.exception is FirebaseAuthInvalidCredentialsException) {
                    showMessage(getString(R.string.invalid_otp))
                    binding.txtOtp.error = getString(R.string.invalid_otp)
                } else {
                    showMessage(getString(R.string.an_error_unknow))
                }
            }
        }
    }

    private fun countTimeOutSms() {
        timeOut = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                binding.textInputOTP.visibility = View.GONE
                binding.btnLogin.text = getString(R.string.receive_otp)
                isSendOTPSuccess = false
            }
        }.start()
    }

    private fun showMessage(mess: String) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyBoard()
        return super.dispatchTouchEvent(ev)
    }

    private fun hideKeyBoard(): Boolean {
        val view = currentFocus ?: return false
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun getInfoSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        finish()
    }

    override fun error(err: String?) {
        binding.processLoading.visibility = View.GONE
        showMessage("$err")
    }

    override fun phoneNumberNotExists(phoneNumber: String) {
        Handler(Looper.getMainLooper()).post {
            binding.processLoading.visibility = View.GONE
            if (!isSendSms) {
                phoneNumber.isNotNullOrEmpty {
                    sendSMSCode(it)
                    DataManager.savePhoneLogin(this, it)
                }
            } else {
                phoneNumber.isNotNullOrEmpty {
                    resendSMSCode(it)
                    DataManager.savePhoneLogin(this, it)
                }
            }
        }
    }


}