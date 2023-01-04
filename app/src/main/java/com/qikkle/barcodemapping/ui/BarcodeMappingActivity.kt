package com.qikkle.barcodemapping.ui

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.qikkle.barcodemapping.R
import com.qikkle.barcodemapping.databinding.ActivityBarcodeMappingBinding
import com.qikkle.barcodemapping.ui.login.LoginActivity
import com.qikkle.barcodemapping.utils.PreferenceManager
import com.rscja.barcode.BarcodeDecoder
import com.rscja.barcode.BarcodeFactory

class BarcodeMappingActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var _binding: ActivityBarcodeMappingBinding? = null;
    private val binding get() = _binding!!
    private val mediaPlayer by lazy {
        MediaPlayer.create(this, R.raw.beep);
    }

    private val barcodeReader by lazy {
        BarcodeFactory.getInstance().barcodeDecoder
    }

    var barcodeListener: BarcodeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBarcodeMappingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment?
        navController = navHostFragment!!.navController

        setupActionBarWithNavController(navController)

        barcodeReader.open(this)

        barcodeReader.setDecodeCallback { barcodeEntity ->
            barcodeListener?.let { listener->
                if (barcodeEntity != null && barcodeEntity.resultCode == BarcodeDecoder.DECODE_SUCCESS) {
                    listener.onScanSuccess((barcodeEntity.barcodeData))
                } else {
                    listener.onScanFail()
                }
                playSound()
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout){
            PreferenceManager.remove(this, PreferenceManager.Keys.USER_ID)
            PreferenceManager.remove(this, PreferenceManager.Keys.LOCATION_ID)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun scanBarcode(){
        barcodeReader.startScan()
    }

    private fun playSound() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start()
        }
    }

    interface BarcodeListener {
        fun onScanSuccess(result: String)
        fun onScanFail()
    }
}