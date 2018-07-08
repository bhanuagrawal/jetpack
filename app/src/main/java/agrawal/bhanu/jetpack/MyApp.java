package agrawal.bhanu.jetpack;

import android.app.Application;

import agrawal.bhanu.jetpack.components.DaggerLocalDataComponent;
import agrawal.bhanu.jetpack.components.DaggerWebComponent;
import agrawal.bhanu.jetpack.components.LocalDataComponent;
import agrawal.bhanu.jetpack.modules.AppModule;
import agrawal.bhanu.jetpack.components.WebComponent;
import agrawal.bhanu.jetpack.modules.LocalDataModule;
import agrawal.bhanu.jetpack.modules.NetworkModule;

public class MyApp extends Application {

    private WebComponent webComponent;
    private LocalDataComponent localDataComponent;

    public LocalDataComponent getLocalDataComponent() {
        return localDataComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        webComponent =  DaggerWebComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();

        localDataComponent = DaggerLocalDataComponent.builder()
                .appModule(new AppModule(this))
                .localDataModule(new LocalDataModule())
                .build();


    }

    public WebComponent getWebComponent() {
        return webComponent;
    }
}
