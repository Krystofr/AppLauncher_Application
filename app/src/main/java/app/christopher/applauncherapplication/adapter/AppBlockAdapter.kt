package app.christopher.applauncherapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.christopher.applauncherapplication.data.AppBlock
import app.christopher.applauncherapplication.databinding.ItemAppBinding

class AppBlockAdapter(val context: Context) :
    RecyclerView.Adapter<AppBlockAdapter.AppItemViewHolder>() {
    lateinit var appBinding: ItemAppBinding
    var appList: List<AppBlock>? = null

    inner class AppItemViewHolder(val appBinding: ItemAppBinding) :
        RecyclerView.ViewHolder(appBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        appBinding = ItemAppBinding.inflate(inflater, parent, false)
        return AppItemViewHolder(appBinding)
    }

    override fun onBindViewHolder(holder: AppItemViewHolder, position: Int) {
        holder.appBinding.appIcon.setImageDrawable(appList?.get(position)?.icon)
        holder.appBinding.appName.text = appList?.get(position)?.appName
        holder.appBinding.root.setOnClickListener {
            context.startActivity(
                context.packageManager.getLaunchIntentForPackage(appList?.get(position)?.packageName?:"app.christopher.applauncherapplication")
            )
        }
    }
    fun passAppList(
        appsList: List<AppBlock>
    ){
        appList = appsList
        notifyDataSetChanged()
    }

    override fun getItemCount() = appList?.size?:0
}