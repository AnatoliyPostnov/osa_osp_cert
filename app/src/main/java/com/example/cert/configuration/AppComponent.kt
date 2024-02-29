package com.example.cert.configuration

import com.example.cert.ui.activity.MainActivity
import com.example.cert.ui.activity.OsaMainActivity
import com.example.cert.ui.activity.OspMainActivity
import dagger.Component
import dagger.Module
import dagger.Provides


@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: OsaMainActivity)
    fun inject(activity: OspMainActivity)


    val navigationController: String
}

@Module
object AppModule {

    @Provides
    fun provideNavigationController(): String {
        return "rememberNavController()"
    }
}
