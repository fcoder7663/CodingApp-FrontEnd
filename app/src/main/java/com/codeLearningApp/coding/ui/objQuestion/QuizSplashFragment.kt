package com.codeLearningApp.coding.ui.objQuestion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.codeLearningApp.coding.R
import com.codeLearningApp.coding.viewmodel.ObjQuestionViewModel
import com.codeLearningApp.coding.utils.GlobalData.getLoadingDialog


class QuizSplashFragment : Fragment() {

    private lateinit var viewModel: ObjQuestionViewModel
    private lateinit var dialog: AlertDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_obj_question_splash, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(ObjQuestionViewModel::class.java)

        val seekBar = view.findViewById<SeekBar>(R.id.quiz_splash_fragment_seek_bar)
        val startQuiz = view.findViewById<MaterialButton>(R.id.quiz_splash_fragment_start_quiz_btn)
        val seekBarText = view.findViewById<TextView>(R.id.quiz_splash_fragment_seek_bar_number)
        val testMode = view.findViewById<SwitchMaterial>(R.id.quiz_splash_fragment_test_mode)

        dialog = getLoadingDialog(
            requireActivity(),
            "Loading Quiz ...",
            "Please wait, Good things take time ...")

        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBarText.text = String.format("%02d", progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


        startQuiz.setOnClickListener {

            // start loading
            dialog.show()

            viewModel.repository.quizLoaded.value = false
            viewModel.repository.answers.clear()
            viewModel.repository.objRandomQuestionList.clear()

            val size = seekBar.progress
            viewModel.repository.size = size
            viewModel.repository.testMode = testMode.isChecked

            viewModel.repository.loadObjQuestion()

            viewModel.repository.quizLoaded.observe(requireActivity(), {
                if(it) {
                    dialog.dismiss()
                    // go to quiz main fragment now
                    Navigation.findNavController(context as ObjQuizActivity, R.id.nav_host_quiz_fragment)
                        .navigate(R.id.quizSplashFragment_to_quizMainFragment)
                }
            })

        }

        return view

    }

}