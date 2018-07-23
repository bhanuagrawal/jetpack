package agrawal.bhanu.jetpack.components;


import javax.inject.Singleton;

import agrawal.bhanu.jetpack.launcher.data.AppsRepository;
import agrawal.bhanu.jetpack.launcher.ui.AppList;
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel;
import agrawal.bhanu.jetpack.launcher.ui.DefaultPage;
import agrawal.bhanu.jetpack.modules.AppModule;
import agrawal.bhanu.jetpack.modules.LocalDataModule;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, LocalDataModule.class})
public interface LocalDataComponent {
    void inject(LauncherViewModel launcherViewModel);
    void inject(DefaultPage defaultPage);
    void inject(AppsRepository appsRepository);
    void inject(AppList appList);
}
