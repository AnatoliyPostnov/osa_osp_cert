package com.example.cert.ui.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.cert.domain.model.ExamsDomainDto
import com.example.cert.ui.activity.OsaMainActivity
import com.example.cert.ui.activity.OspMainActivity

class MainMenuDto(
    val examsDomainDto: ExamsDomainDto,
    val pictureId: Int,
    val context: Context,
    val intent: Intent,
    val options: Bundle? = null
) {
    companion object {
        @SuppressLint("DiscouragedApi")
        fun createExamActivity(context: Context, savedInstanceState: Bundle?, exam: ExamsDomainDto): MainMenuDto {
            val intent = if (exam.pictureFileName == "osa") {
                Intent(context, OsaMainActivity::class.java)
            } else {
                Intent(context, OspMainActivity::class.java)
            }
            intent.putExtra("exam_id", exam.examId)

            return MainMenuDto(
                exam,
                context.resources.getIdentifier(exam.pictureFileName, "drawable", context.packageName),
                context,
                intent,
                savedInstanceState
            )
        }
    }
}
