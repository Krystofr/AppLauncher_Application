package app.christopher.applauncherapplication

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.christopher.applauncherapplication.adapter.WeatherAdapter
import app.christopher.applauncherapplication.databinding.ActivityMainBinding
import app.christopher.applauncherapplication.launcher.LauncherActivity
import app.christopher.applauncherapplication.remote.WeatherApi
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "MainActivity"
//@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val PERMISSION_CODE: Int = 1
    private var location: Location? = null
    private var locationManager: LocationManager? = null
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var weatherAdapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        this.registerReceiver(broadcastReceiver, iFilter)

        mainBinding.apply {
            launcherBtn.setOnClickListener {
                startActivity(Intent(this@MainActivity, LauncherActivity::class.java))
            }
                weatherRv.apply {
                    weatherAdapter = WeatherAdapter()
                    adapter = weatherAdapter
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    weatherAdapter.notifyDataSetChanged()
                }
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // PERMISSION CHECKING
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            ) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_CODE)
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_CODE)
            }
        }
        //Get last known location of user
        location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        lifecycleScope.launchWhenCreated {
            val rest = try {
                WeatherApi.restApi.getWeatherApi()
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d(TAG, e.localizedMessage!!.toString())
                Toast.makeText(this@MainActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
                return@launchWhenCreated
            } catch (ex: HttpException) {
                ex.printStackTrace()
                Log.d(TAG, ex.localizedMessage!!.toString())
                Toast.makeText(this@MainActivity, ex.localizedMessage, Toast.LENGTH_SHORT).show()
                return@launchWhenCreated
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(this, "Permission Denied! Please Proved the permissions", Toast.LENGTH_SHORT).show()
                finish()
            }
            return
        }
    }
    private val broadcastReceiver : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(mContext: Context?, intent: Intent?) {
            val level : Int = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            mainBinding.batteryLevel.text = "$level%"
        }
    }
}