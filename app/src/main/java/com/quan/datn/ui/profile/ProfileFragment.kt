package com.quan.datn.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.nth.filepicker.MimeTypeManager
import com.nth.filepicker.NTHFilePicker
import com.nth.filepicker.engine.impl.GlideEngine
import com.nth.filepicker.internal.entity.CaptureStrategy
import com.nth.filepicker.internal.entity.ConstValue
import com.nth.filepicker.internal.utils.Platform
import com.quan.datn.MainActivity
import com.quan.datn.R
import com.quan.datn.databinding.FragmentProfileBinding
import com.quan.datn.model.ProfileModel
import com.quan.datn.model.UpdateInfoRequest
import com.quan.datn.ui.utils.DataManager
import com.quan.datn.ui.utils.popup.PopupImage
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.*

class ProfileFragment : Fragment(), ProfileCallback {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var mDateSetListener: DatePickerDialog.OnDateSetListener
    private var currentProfile:ProfileModel? = null
    private var pathFileAvatar:String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        currentProfile = DataManager.getInfoSessionLogin(this.requireContext())
        binding.profile = currentProfile
        viewModel.callBack = this

        viewModel.userProfile.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.profile = it
        })

        selectBirthday()

        binding.imgAvatar.setOnClickListener {
            selectImageAvatar()
        }

        binding.btnSaveProfile.setOnClickListener {
            (activity as MainActivity).onHideKeyBoard()
            updateProfile()
        }

        return binding.root
    }

    private fun updateProfile() {
        if (currentProfile != null){
            val gt: String = if (binding.radioSexFemale.isChecked){
                getString(R.string.register_sex_female)
            }else{
                getString(R.string.register_sex_male)
            }
            val request = UpdateInfoRequest(
                currentProfile!!.id,
                currentProfile!!.maBN,
                binding.txtHo.text.toString(),
                binding.txtTen.text.toString(),
                gt,
                binding.txtBirthDay.text.toString(),
                binding.txtNgheNghiep.text.toString(),
                binding.txtAddress.text.toString(),
                binding.txtPhoneNumber.text.toString()
                )
            viewModel.updateFile(request,pathFileAvatar)
        }else{
            Toast.makeText(this.requireContext(),R.string.an_error_unknow,Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectBirthday() {
        mDateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                var mMonth = month
                mMonth += 1
                val date = "$day-$mMonth-$year"
                binding.txtBirthDay.text = " $date"
            }

        binding.txtBirthDay.setOnClickListener {
            val cal: Calendar = Calendar.getInstance()
            val year: Int = cal.get(Calendar.YEAR)
            val month: Int = cal.get(Calendar.MONTH)
            val day: Int = cal.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(
                this.requireContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day
            )
            dialog.datePicker.maxDate = Date().time
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }

    private fun selectImageAvatar() {
        PopupImage(
            this.requireContext(),
            binding.imgAvatar,
            object : PopupImage.IPopupChooseImage {
                override fun chooseLibrary() {
                    checkPermissionRWFile()
                }

                @SuppressLint("CheckResult")
                override fun viewImage() {

                }
            }).show()
    }

    @SuppressLint("CheckResult")
    private fun checkPermissionRWFile() {
        RxPermissions((this@ProfileFragment.activity as MainActivity))
            .request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .subscribe {
                if (!it) {
                    Toast.makeText(
                        this@ProfileFragment.requireContext(),
                        getString(R.string.not_permission),
                        Toast.LENGTH_LONG
                    ).show()
                    return@subscribe
                } else {
                    openGallery()
                }
            }
    }

    private fun openGallery() {
        NTHFilePicker.from(this@ProfileFragment)
            .choose(MimeTypeManager.ofImage())
            .maxSelectable(1)
            .countable(false)
            .capture(true)
            .captureStrategy(
                CaptureStrategy(
                    true,
                    "${Platform.getPackageName(this@ProfileFragment.requireContext())}.fileprovider"
                )
            )
            .thumbnailScale(0.85f)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            .imageEngine(GlideEngine())
            .forResult(ConstValue.REQUEST_CODE_CHOOSE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return

        if (requestCode == ConstValue.REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            val uriList = NTHFilePicker.obtainPathResult(data) ?: return
            uriList.forEach {
                pathFileAvatar = it
                Log.i("NTHDebug", it)
            }
            Glide.with(this).load(uriList[0])
                .into(binding.imgAvatar)
        }
    }

    override fun updateSuccess(profile: ProfileModel) {
        DataManager.saveSessionLogin(this.requireContext(),profile)
        showMessage(getString(R.string.update_profile_success))
        (activity as MainActivity).updateInfoBenhNhan()
    }

    override fun updateFalse(err: String?) {
        showMessage(err)
    }

    private fun showMessage(mess:String?){
        Toast.makeText(this.requireContext(),mess,Toast.LENGTH_SHORT).show()
    }

}