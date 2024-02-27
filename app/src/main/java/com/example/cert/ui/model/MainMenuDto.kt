package com.example.cert.ui.model

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.cert.R
import com.example.cert.ui.activity.OsaMainActivity
import com.example.cert.ui.activity.OspMainActivity

class MainMenuDto(
    val buttonName: String,
    val pictureId: Int,
    val context: Context,
    val intent: Intent,
    val options: Bundle? = null
) {
    companion object {
        fun createOsaActivity(context: Context, savedInstanceState: Bundle?): MainMenuDto {
            return MainMenuDto(
                "Start preparing OSA exam",
                R.drawable.osa,
                context,
                Intent(context, OsaMainActivity::class.java),
                savedInstanceState
            )
        }

        fun createOspActivity(context: Context, savedInstanceState: Bundle?): MainMenuDto {
            return MainMenuDto(
                "Start preparing OSP exam",
                R.drawable.osp,
                context,
                Intent(context, OspMainActivity::class.java),
                savedInstanceState
            )
        }
    }
}
