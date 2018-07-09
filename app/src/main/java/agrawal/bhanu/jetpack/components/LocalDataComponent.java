package agrawal.bhanu.jetpack.components;


import javax.inject.Singleton;

import agrawal.bhanu.jetpack.MainActivity;
import agrawal.bhanu.jetpack.launcher.ui.AppsViewModel;
import agrawal.bhanu.jetpack.modules.AppModule;
import agrawal.bhanu.jetpack.modules.LocalDataModule;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, LocalDataModule.class})
public interface LocalDataComponent {
    void inject(AppsViewModel appsViewModel);
}
