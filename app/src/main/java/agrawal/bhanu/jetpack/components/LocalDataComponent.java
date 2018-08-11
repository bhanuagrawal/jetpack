package agrawal.bhanu.jetpack.components;


import javax.inject.Singleton;

import agrawal.bhanu.jetpack.launcher.data.AppsRepository;
import agrawal.bhanu.jetpack.launcher.ui.allapps.AppList;
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel;
import agrawal.bhanu.jetpack.launcher.ui.defaultpage.AppsFolderAdapter;
import agrawal.bhanu.jetpack.launcher.ui.defaultpage.DefaultPage;
import agrawal.bhanu.jetpack.launcher.ui.folder.AppsFolder;
import agrawal.bhanu.jetpack.launcher.ui.viewholder.AppViewHolder;
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
    void inject(AppsFolderAdapter appsFolderAdapter);
    void inject(AppsFolder appsFolder);

    void inject(AppViewHolder appViewHolder);
}
