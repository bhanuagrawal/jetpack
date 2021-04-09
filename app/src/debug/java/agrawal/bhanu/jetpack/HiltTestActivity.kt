/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package agrawal.bhanu.jetpack

import agrawal.bhanu.jetpack.launcher.ui.Home
import agrawal.bhanu.jetpack.launcher.ui.allapps.AppList
import agrawal.bhanu.jetpack.launcher.ui.allapps.Apps
import agrawal.bhanu.jetpack.launcher.ui.defaultpage.DefaultPage
import agrawal.bhanu.jetpack.launcher.ui.folder.AppsFolder
import agrawal.bhanu.jetpack.launcher.ui.folder.AppsFolderDialogFragmnet
import agrawal.bhanu.jetpack.reddit.ui.ItemsList
import android.net.Uri
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.test.espresso.IdlingResource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity(),  ItemsList.OnFragmentInteractionListener,
AppList.OnFragmentInteractionListener,
Apps.OnFragmentInteractionListener,
Home.OnFragmentInteractionListener,
DefaultPage.OnFragmentInteractionListener,
AppsFolder.OnFragmentInteractionListener,
AppsFolderDialogFragmnet.OnFragmentInteractionListener {


    @Inject
    lateinit var mIdlingResource: SimpleIdlingResource


    override fun onFragmentInteraction(uri: Uri?) {
    }

    override fun onFragmentCreated(fragment: Fragment?) {
    }
}
