package www.smktelkommlg.sch.recylcerview

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import www.smktelkommlg.sch.recylcerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<Hero>()
    private var title = "mode list"
    private var channel_id = "NOTIFY"
    private var notificationId = 1092

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvHeroes.setHasFixedSize(true)

        list.addAll(getListHeroes())
        showRecyclerList()
        setActionBarTitle(title)

        createNotificationChannel()
    }

    private fun showSelectedHero(hero:Hero){
        Toast.makeText(this, "kamu memilih ${hero.name}", Toast.LENGTH_SHORT).show()
    }

    private fun getListHeroes():ArrayList<Hero>{
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.getStringArray(R.array.data_photo)

        val listHero = ArrayList<Hero>()

        for (position in dataName.indices){
            val hero = Hero(
                dataName[position],
                dataDescription[position],
                dataPhoto[position]
            )
            listHero.add(hero)
        }

        return listHero
    }

    private fun setActionBarTitle(title: String?) {
        supportActionBar?.title = title
    }

    private fun showRecyclerList(){
        binding.rvHeroes.layoutManager = LinearLayoutManager(this)
        val listHeroAdapter = ListHeroAdapter(list)
        binding.rvHeroes.adapter = listHeroAdapter

        listHeroAdapter.setOnItemClickCallback(object : ListHeroAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Hero) {
                showSelectedHero(data)
            }
        })
    }

    private fun showRecyclerGrid(){
        binding.rvHeroes.layoutManager = GridLayoutManager(this, 2)
        val gridHeroAdapter = GridHeroAdapter(list)
        binding.rvHeroes.adapter = gridHeroAdapter

        gridHeroAdapter.setOnItemClickCallback(object : GridHeroAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Hero) {
                showSelectedHero(data)
            }
        })
    }

    private fun showRecyclerCardView() {
        binding.rvHeroes.layoutManager = LinearLayoutManager(this)
        val cardViewHeroAdapter = CardViewHeroAdapter(list)
        binding.rvHeroes.adapter = cardViewHeroAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode:Int){
        when(selectedMode){
            R.id.action_list ->{
                title = "Mode List"
                showRecyclerList()
            }
            R.id.action_grid ->{
                title = "Mode Grid"
                showRecyclerGrid()
            }
            R.id.action_cardview ->{
                title = "Mode CardView"
                showRecyclerCardView()
            }
            R.id.get_notification ->{
                sendNotification()
            }
        }
        setActionBarTitle(title)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "NOTIFY HEHE"
            val descriptionText = "Ini adalah deskripsi notifikasi"
            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
            val channel: NotificationChannel = NotificationChannel(channel_id,name,importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }
    private fun sendNotification(){
        val builder = NotificationCompat.Builder(this,channel_id)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Ada notif nih!")
            .setContentText("Buka x")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(notificationId,builder.build())
        }
    }
}