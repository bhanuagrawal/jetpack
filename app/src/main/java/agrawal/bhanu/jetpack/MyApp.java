package agrawal.bhanu.jetpack;

import android.app.Application;

import agrawal.bhanu.jetpack.components.DaggerWebComponent;
import agrawal.bhanu.jetpack.modules.AppModule;
import agrawal.bhanu.jetpack.components.WebComponent;
import agrawal.bhanu.jetpack.modules.WebModule;

public class MyApp extends Application {

    private WebComponent webComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        webComponent =  DaggerWebComponent.builder()
                .appModule(new AppModule(this))
                .webModule(new WebModule())
                .build();
    }

    public WebComponent getWebComponent() {
        return webComponent;
    }
}
