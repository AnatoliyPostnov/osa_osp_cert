package com.example.cert.ui.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.cert.R
import com.example.cert.domain.model.ExamsDomainDto
import com.example.cert.ui.activity.OsaMainActivity
import com.example.cert.ui.activity.OspMainActivity
import com.example.cert.ui.viewmodel.MainActivityViewModel

class MainMenuDto(
    val buttonName: String,
    val pictureId: Int,
    val context: Context,
    val intent: Intent,
    val options: Bundle? = null
) {
    companion object {
        @SuppressLint("DiscouragedApi")
        fun createExamActivity(context: Context, savedInstanceState: Bundle?, exam: ExamsDomainDto): MainMenuDto {
            return MainMenuDto(
                exam.content,
                context.resources.getIdentifier(exam.pictureFileName, "drawable", null),
                context,
                Intent(context, OsaMainActivity::class.java),
                savedInstanceState
            )
        }
    }
}
