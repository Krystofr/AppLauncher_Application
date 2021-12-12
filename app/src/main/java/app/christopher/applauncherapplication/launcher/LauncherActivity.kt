package app.christopher.applauncherapplication.launcher

import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import app.christopher.applauncherapplication.adapter.AppBlockAdapter
import app.christopher.applauncherapplication.data.AppBlock
import app.christopher.applauncherapplication.databinding.ActivityLauncherBinding

class LauncherActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityLauncherBinding
    private lateinit var resolvedApplist: List<ResolveInfo>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        mainBinding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        resolvedApplist = packageManager
            .queryIntentActivities(Intent(Intent.ACTION_MAIN,null)
                .addCategory(Intent.CATEGORY_LAUNCHER),0)
        val appList = ArrayList<AppBlock>()

        for (ri in resolvedApplist) {
            if(ri.activityInfo.packageName!=this.packageName) {
                val app = AppBlock(
                    ri.loadLabel(packageManager).toString(),
                    ri.activityInfo.loadIcon(packageManager),
                    ri.activityInfo.packageName
                )
                appList.add(app)
            }
        }
        mainBinding.appList.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL )
        //mainBinding.appList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        mainBinding.appList.adapter = AppBlockAdapter(this).also {
            it.passAppList(appList.sortedWith { o1, o2 ->
                o1?.appName?.compareTo(o2?.appName ?: "",
                    true) ?: 0;
            })
        }
    }
}