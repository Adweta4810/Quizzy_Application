package com.dma.studentapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dma.studentapplication.data.local.room.QuizDatabase
import com.dma.studentapplication.navigation.AppNavGraph
import com.dma.studentapplication.repository.QuizRepository
import com.dma.studentapplication.ui.theme.StudentApplicationTheme
import com.dma.studentapplication.viewmodel.QuizViewModel
import com.dma.studentapplication.viewmodel.QuizViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val db = QuizDatabase.getInstance(this)
        val repository = QuizRepository(db.quizResultDao(), this)
        val vmFactory = QuizViewModelFactory(repository)

        setContent {
            StudentApplicationTheme {
                val viewModel: QuizViewModel = viewModel(factory = vmFactory)

                AppNavGraph(
                    viewModel = viewModel
                )
            }
        }
    }
}