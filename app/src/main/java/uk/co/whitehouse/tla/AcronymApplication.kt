package uk.co.whitehouse.tla

import android.app.Application
import uk.co.whitehouse.tla.data.AppContainer
import uk.co.whitehouse.tla.data.AppDataContainer

class AcronymApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

}