package com.enparadigm_weather.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.enparadigm_weather.R
import com.enparadigm_weather.data.network.MyApi
import com.enparadigm_weather.data.repository.Repository
import com.enparadigm_weather.databinding.ActivityMainBinding
import com.enparadigm_weather.model.daily.DailyWeather
import com.enparadigm_weather.model.daily.Data
import com.enparadigm_weather.util.BindingUtils
import com.enparadigm_weather.util.isInternetAvailable
import com.kfd.esasyakshetra.util.Coroutines
import kotlinx.android.synthetic.main.activity_main.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var mDrawerToggle: ActionBarDrawerToggle? = null
    lateinit var repository: Repository
    lateinit var binding: ActivityMainBinding

    private var dailyWeatherAdapter: DailyWeatherAdapter? = null
    private var hourlyWeatherAdapter: HourlyWeatherAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()

    }


    private fun init() {
        setSupportActionBar(toolbar)
        val api = MyApi()
        repository = Repository(api)
        val w = window // in Activity's onCreate() for instance
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
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
        val search = menu.findItem(R.id.search)
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


    override fun onStart() {
        super.onStart()
        main_container.setBackground(resources.getDrawable(BindingUtils.getBackgroundImage()))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        searchDummy()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun searchDummy() {
        if (!isInternetAvailable()) {
            Toast.makeText(this, "Make sure you have an active data connection", Toast.LENGTH_SHORT)
                .show()
            return
        }
        Coroutines.io {
            try {
                val response: DailyWeather = repository.searchCity("Raleigh,NC")
                updateDailyWeather(response)
                updateHourlyWeather(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    private fun updateDailyWeather(dailyWeather: DailyWeather) {
        val dailyData: List<Data> = this.getPresentDailyData(dailyWeather)!!
        setDailyWeatherHeader(dailyData[0])
        setDailyWeatherAdapter(dailyData)
    }

    private fun updateHourlyWeather(response: DailyWeather) {
        val dailyData: List<Data> = this.getPresentDailyData(response)!!
        binding.weather = response
        binding.data = response.data.get(0)
        setHeaderHourlyWeather(dailyData[0])
        setHourlyWeatherAdapter(dailyData)
    }

    private fun setHeaderHourlyWeather(currentHourlyWeather: Data) {
        try {
            /*no free call for hourly based api*/
//            val date = f.parse(currentHourlyWeather.datetime)
            @SuppressLint("SimpleDateFormat") val now =
                SimpleDateFormat("h:mm aa").format(System.currentTimeMillis())
            runOnUiThread {
                current_time.setText(now)
                weather_image.setImageDrawable(
                    BindingUtils.getWeatherIcon(
                        this@MainActivity
                        , currentHourlyWeather.weather.icon
                    )
                )
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun setHourlyWeatherAdapter(currentHourlyWeather: List<Data>) {
        if (currentHourlyWeather.size > 0) {
            runOnUiThread {
                if (currentHourlyWeather.size > 6) {
                    hourlyWeatherAdapter = HourlyWeatherAdapter(
                        this@MainActivity,
                        currentHourlyWeather.subList(0, 5),
                        false
                    )
                } else {
                    hourlyWeatherAdapter =
                        HourlyWeatherAdapter(this@MainActivity, currentHourlyWeather, false)
                }
                hourly_weather_report.setLayoutManager(
                    GridLayoutManager(
                        this@MainActivity,
                        hourlyWeatherAdapter!!.getItemCount()
                    )
                )
                hourly_weather_report.setAdapter(hourlyWeatherAdapter)
                hourly_container.setVisibility(View.VISIBLE)
                /* constraintLayoutHourlyContainer.setOnClickListener({ view ->
                     linearLayoutFragmentContainer.setVisibility(View.VISIBLE)
                     val bundle = Bundle()
                     bundle.putSerializable("data", currentHourlyWeather as Serializable)
                     val hourlyWeatherFragment = HourlyWeatherFragment()
                     hourlyWeatherFragment.setArguments(bundle)
                     setFragment(hourlyWeatherFragment)
                 })*/
            }
        }
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


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onQueryTextSubmit(p0: String?): Boolean {
        toolbar.collapseActionView()
        searchDummy()
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
    }
}