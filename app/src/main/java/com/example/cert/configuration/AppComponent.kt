package com.example.cert.configuration

import com.example.cert.data.client.BackendCommunicationServiceImpl
import com.example.cert.data.repository.QuestionActivityRepositoryImpl
import com.example.cert.data.repository.itrf.BackendCommunicationService
import com.example.cert.domain.repository.QuestionActivityRepository
import com.example.cert.ui.activity.MainActivity
import com.example.cert.ui.activity.ThemesMainActivity
import com.example.cert.ui.activity.question.QuestionsActivity
import com.example.cert.ui.activity.question.QuestionsResultActivity
import com.example.cert.ui.viewmodel.Factory
import dagger.Binds
import dagger.Component
import dagger.Module


@Component(modules = [AppModule::class, JacksonConfig::class, BindsModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: ThemesMainActivity)
    fun inject(activity: QuestionsActivity)
    fun inject(activity: QuestionsResultActivity)

    val factory: Factory
}

@Module
object AppModule

@Module
abstract class BindsModule {
    @Binds
    abstract fun bindBackendCommunicationServiceImpl(
        backendCommunicationServiceImpl: BackendCommunicationServiceImpl
    ): BackendCommunicationService

    @Binds
    abstract fun bindQuestionActivityRepositoryImpl(
        questionActivityRepositoryImpl: QuestionActivityRepositoryImpl
    ): QuestionActivityRepository
}