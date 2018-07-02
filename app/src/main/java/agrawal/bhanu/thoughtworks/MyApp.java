package agrawal.bhanu.thoughtworks;

import android.app.Application;

import agrawal.bhanu.thoughtworks.components.DaggerWebComponent;
import agrawal.bhanu.thoughtworks.modules.AppModule;
import agrawal.bhanu.thoughtworks.components.WebComponent;
import agrawal.bhanu.thoughtworks.modules.WebModule;

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
