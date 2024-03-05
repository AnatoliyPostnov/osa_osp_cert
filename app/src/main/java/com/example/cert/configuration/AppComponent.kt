package com.example.cert.configuration

import com.example.cert.data.client.BackendCommunicationServiceImpl
import com.example.cert.data.repository.OsaMainActivityRepositoryImpl
import com.example.cert.data.repository.itrf.BackendCommunicationService
import com.example.cert.domain.repository.OsaMainActivityRepository
import com.example.cert.ui.activity.MainActivity
import com.example.cert.ui.activity.OsaMainActivity
import com.example.cert.ui.activity.OspMainActivity
import com.example.cert.ui.activity.TestActivity
import com.example.cert.ui.viewmodel.Factory
import dagger.Binds
import dagger.Component
import dagger.Module


@Component(modules = [AppModule::class, JacksonConfig::class, BindsModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: OsaMainActivity)
    fun inject(activity: OspMainActivity)
    fun inject(activity: TestActivity)

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
    abstract fun bindOsaMainActivityRepositoryImpl(
        osaMainActivityRepositoryImpl: OsaMainActivityRepositoryImpl
    ): OsaMainActivityRepository
}