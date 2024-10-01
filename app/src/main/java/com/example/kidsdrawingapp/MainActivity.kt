package com.example.kidsdrawingapp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.example.kidsdrawingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawingView: DrawingView
    private var mImageButtonCurrentPaint: ImageButton? = null
    private lateinit var linearLayoutPaintColors: LinearLayout
    val requestPermission:ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value

                if (isGranted){
                    Toast.makeText(this@MainActivity,"Permissipn Granted",Toast.LENGTH_LONG).show()
                }else{
                    if (permissionName == android.Manifest.permission.READ_EXTERNAL_STORAGE){
                        Toast.makeText(this@MainActivity,"Permission Denied",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    val openGalleryLauncher:ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
            if (result.resultCode == RESULT_OK && result.data!=null ){
                val imageBackground: ImageView = findViewById(R.id.iv_background)
                imageBackground.setImageURI(result.data?.data)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        drawingView = binding.drawingView
        val ibBrush: ImageButton = findViewById(R.id.ib_brush)

        binding.drawingView.setSizeForBrush(20.toFloat())
        linearLayoutPaintColors=binding.llPaintColors
        mImageButtonCurrentPaint=linearLayoutPaintColors[2] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
        )

        binding.ibBrush.setOnClickListener {
            showBrushSizeChooserDialog()
        }
    }

    val ibGallery: ImageButton = findViewById(R.id.ib_gallery)



    val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
   // openGalleryLauncher.launch(pickIntent)


    private fun showBrushSizeChooserDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.diolag_brush_size)
        brushDialog.setTitle("Brush size: ")
        val smallBtn = brushDialog.findViewById<ImageButton>(R.id.ib_small_brush)
        smallBtn.setOnClickListener {
            drawingView.setSizeForBrush(5.toFloat())
            brushDialog.dismiss()
        }
        val mediumBtn = brushDialog.findViewById<ImageButton>(R.id.ib_medium_brush)
        mediumBtn.setOnClickListener {
            drawingView.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }
        val largeBtn = brushDialog.findViewById<ImageButton>(R.id.ib_large_brush)
        largeBtn.setOnClickListener {
            drawingView.setSizeForBrush(15.toFloat())
            brushDialog.dismiss()
        }

        brushDialog.show()
    }
    fun paintClicked(view:View){
        if (view !== mImageButtonCurrentPaint){
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawingView?.setColor(colorTag)

            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_pressed)
            )
            mImageButtonCurrentPaint?.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_normal)
            )
            mImageButtonCurrentPaint = view

        }

    }
}