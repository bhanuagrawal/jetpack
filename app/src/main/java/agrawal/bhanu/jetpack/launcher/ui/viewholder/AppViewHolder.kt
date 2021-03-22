package agrawal.bhanu.jetpack.launcher.ui.viewholder

import agrawal.bhanu.jetpack.R
import agrawal.bhanu.jetpack.databinding.PopupWindowBinding
import agrawal.bhanu.jetpack.launcher.data.entities.App
import agrawal.bhanu.jetpack.launcher.ui.AppsAdapter
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel
import agrawal.bhanu.jetpack.launcher.ui.defaultpage.AppsFolderAdapter
import agrawal.bhanu.jetpack.launcher.util.callbacks.Callback
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppViewHolder @Inject constructor(binding: ViewBinding, viewType: Int, context: Context) : RecyclerView.ViewHolder(binding.getRoot()), View.OnClickListener, OnLongClickListener {
    private val context: Context
    private val mAppsModel: LauncherViewModel
    private var app: App? = null
    var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    private var pm: PackageManager? = null
    @JvmField
    var popupWindow: PopupWindow? = null

    @JvmField
    @Inject
    var executor: Executor? = null
    override fun onClick(view: View) {
        val position = adapterPosition
        pm = context.packageManager
        try {
            if (itemViewType == AppsAdapter.FOLDER_DIALOG) {
                (context as Activity).onBackPressed()
            }
            val intent =( pm as PackageManager).getLaunchIntentForPackage(app!!.appPackage)
            intent!!.addCategory(Intent.CATEGORY_LAUNCHER)
            if (intent == null) {
                throw PackageManager.NameNotFoundException()
            } else {
                mAppsModel.onAppSelected(app)
                context.startActivity(intent)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setApp(app: App?) {
        this.app = app
    }

    override fun onLongClick(view: View): Boolean {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupWindowBinding = PopupWindowBinding.inflate(layoutInflater, null)
        popupWindow = PopupWindow(popupWindowBinding.root, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        popupWindow!!.showAsDropDown(itemView.findViewById(R.id.app_icon))
        popupWindow!!.isFocusable = true
        popupWindow!!.update()
        popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (itemViewType == AppsAdapter.ALL_APPS) {
            popupWindowBinding.addToHome.visibility = View.VISIBLE
            popupWindowBinding.addToHome.setOnClickListener {
                popupWindow!!.dismiss()
                mAppsModel.addToHome(app, object : Callback {
                    override fun onSuccess() {
                        (context as AppCompatActivity).runOnUiThread { context.onBackPressed() }
                    }

                    override fun onError(message: String) {
                        (context as AppCompatActivity).runOnUiThread { Toast.makeText(context, message, Toast.LENGTH_LONG).show() }
                    }
                })
            }
        }
        if (itemViewType == AppsFolderAdapter.APP_CONTAINER) {
            popupWindowBinding.remove.visibility = View.VISIBLE
            popupWindowBinding.remove.setOnClickListener {
                popupWindow!!.dismiss()
                mAppsModel.removeFromHome(adapterPosition)
            }
        }
        popupWindowBinding.infoUninstall.visibility = View.VISIBLE
        popupWindowBinding.infoUninstall.setOnClickListener {
            popupWindow!!.dismiss()
            intent.data = Uri.parse("package:" + app!!.appPackage)
            context.startActivity(intent)
        }
        return true
    }

    init {
        if (viewType != AppsAdapter.FOLDER) {
            itemView.findViewById<ConstraintLayout>(R.id.parentlayout).setOnLongClickListener(this)
        }
        itemView.findViewById<TextView>(R.id.app_name).setVisibility(if (viewType == AppsAdapter.ALL_APPS || viewType == AppsAdapter.FOLDER_DIALOG || viewType == AppsFolderAdapter.APP_CONTAINER) View.VISIBLE else View.GONE)
        if (viewType != AppsAdapter.FOLDER) {
            itemView.findViewById<ConstraintLayout>(R.id.parentlayout).setOnClickListener(this)
        }
        this.context = context
        mAppsModel = ViewModelProviders.of((context as AppCompatActivity)).get(LauncherViewModel::class.java)
    }
}