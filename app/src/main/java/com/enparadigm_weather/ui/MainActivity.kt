package com.enparadigm_weather.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.enparadigm_weather.R
import com.enparadigm_weather.data.network.MyApi
import com.enparadigm_weather.data.repository.Repository
import com.enparadigm_weather.model.daily.DailyWeather
import com.enparadigm_weather.model.daily.Data
import com.enparadigm_weather.util.BindingUtils
import com.enparadigm_weather.util.Constants.LAST_SEARCHED_CITY
import com.kfd.esasyakshetra.data.network.NetworkConnectionInterceptor
import com.kfd.esasyakshetra.util.AppPref
import com.kfd.esasyakshetra.util.Coroutines
import kotlinx.android.synthetic.main.activity_main.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null
    var location: Location? = null
    lateinit var appPref: AppPref
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    var dailyWeather: DailyWeather? = null
    lateinit var repository: Repository

    private var dailyWeatherAdapter: DailyWeatherAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val interceptor = NetworkConnectionInterceptor(this)
        val api = MyApi(interceptor)
        repository = Repository(api)
        init()

    }


    private fun init() {
        locationManager =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.d("sarath", "Listener location: " + location.latitude)
                this@MainActivity.location = location
            }

            override fun onStatusChanged(
                provider: String,
                status: Int,
                extras: Bundle
            ) {
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        appPref = AppPref.getInstance()!!
        val lastSearchedCity: String = appPref.getString(LAST_SEARCHED_CITY, null).toString()
        lastSearchedCity?.let { searchCity(it) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window // in Activity's onCreate() for instance
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        mNavigationView.setNavigationItemSelectedListener({ menuItem ->
            mDrawerLayout.closeDrawers()
            menuItem.setChecked(true)
            when (menuItem.getItemId()) {
            }
            true
        })
        mDrawerToggle = ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        mDrawerToggle!!.setDrawerIndicatorEnabled(false)
        mDrawerToggle!!.setToolbarNavigationClickListener({ view ->
            mDrawerLayout.openDrawer(
                GravityCompat.START
            )
        })
        mDrawerToggle!!.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        var search = menu.findItem(R.id.search)
        val mSearchView = search.actionView as SearchView
        mSearchView.setQueryHint(resources.getString(R.string.search_hint))
        mSearchView.setIconifiedByDefault(true)
        mSearchView.setOnQueryTextListener(this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.location -> {
                toolbar.collapseActionView()
                Toast.makeText(this, "under development", Toast.LENGTH_SHORT).show()
//                searchWeatherByLocation()
                true
            }
            else -> false
        }
    }

    private fun searchCity(city: String) {

//            loadDailyWeather(city)

    }

    override fun onStart() {
        super.onStart()
        main_container.setBackground(resources.getDrawable(BindingUtils.getBackgroundImage()))
    }

    override fun onResume() {
        super.onResume()
//        if (dailyWeather != null) {
//            updateDailyWeather()
//        }
        searchDummy()
    }

    private fun searchDummy() {
        Coroutines.main {
            val response: DailyWeather = repository.searchCity("Raleigh,NC")
            Log.i("dddd", "searchDummy: " + response.city_name)
            updateDailyWeather(response)

        }

    }

    private fun updateDailyWeather(dailyWeather: DailyWeather) {
        val dailyData: List<Data> = this!!.getPresentDailyData(dailyWeather!!)!!
        setDailyWeatherHeader(dailyData[0])
        setDailyWeatherAdapter(dailyData)
    }

    fun getPresentDailyData(dailyWeather: DailyWeather): List<Data>? {
        val localDailyData: MutableList<Data> =
            ArrayList()
        for (i in 0 until dailyWeather.data.size) {
            val datum: Data = dailyWeather.data.get(i)
            var postTime = 0
            @SuppressLint("SimpleDateFormat") val f =
                SimpleDateFormat("yyy-MM-dd")
            try {
                val d = f.parse(datum.datetime)
                postTime = d.date
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val currentTime = Date().date
            if (currentTime <= postTime) localDailyData.add(datum)
        }
        return localDailyData
    }

    @SuppressLint("SimpleDateFormat")
    private fun setDailyWeatherHeader(dailyDatum: Data) {
        var f = SimpleDateFormat("yyy-MM-dd")
        try {
            val d = f.parse(dailyDatum.datetime)
            f = SimpleDateFormat("dd-MMM")
            val date = f.format(d)
            runOnUiThread { current_date.setText(date) }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun setDailyWeatherAdapter(dailyData: List<Data>) {
        if (dailyData.size > 0) {
            runOnUiThread {
                if (dailyData.size > 6) {
                    dailyWeatherAdapter =
                        DailyWeatherAdapter(this@MainActivity, dailyData.subList(0, 5), false)
                } else {
                    dailyWeatherAdapter = DailyWeatherAdapter(this@MainActivity, dailyData, false)
                }
                daily_weather_report.setLayoutManager(
                    GridLayoutManager(
                        this@MainActivity, dailyWeatherAdapter!!.itemCount
                    )
                )
                daily_weather_report.setAdapter(dailyWeatherAdapter)
                dailyWeatherAdapter!!.notifyDataSetChanged()
                daily_container.setVisibility(View.VISIBLE)
                /*  daily_container.setOnClickListener({
                          view ->
                      fragment_container.setVisibility(View.VISIBLE)
                      val bundle = Bundle()
                      bundle.putSerializable("data", dailyData as Serializable)
                      val dailyWeatherFragment = DailyWeatherFragment()
                      dailyWeatherFragment.setArguments(bundle)
                      setFragment(dailyWeatherFragment)
                  })*/
            }
        }
    }


    override fun onQueryTextSubmit(p0: String?): Boolean {
        toolbar.collapseActionView()
        searchCity(p0.toString())
        Toast.makeText(this, p0.toString(), Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
    }
}