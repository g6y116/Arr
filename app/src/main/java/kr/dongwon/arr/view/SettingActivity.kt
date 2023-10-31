package kr.dongwon.arr.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.dongwon.arr.R
import kr.dongwon.arr.databinding.ActivitySettingBinding
import kr.dongwon.arr.viewmodel.SettingViewModel

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {

    private val binding: ActivitySettingBinding by lazy { ActivitySettingBinding.inflate(layoutInflater) }
    val viewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out)
    }
}