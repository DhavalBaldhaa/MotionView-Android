package com.dhavalbaldha.motionview_android

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dhavalbaldha.motionview.MotionView
import com.dhavalbaldha.motionview.entity.ImageEntity
import com.dhavalbaldha.motionview.entity.MotionEntity
import com.dhavalbaldha.motionview.entity.TextEntity
import com.dhavalbaldha.motionview.ui.TextEditorDialog
import com.dhavalbaldha.motionview.utils.FontProvider
import com.dhavalbaldha.motionview.viewmodel.Font
import com.dhavalbaldha.motionview.viewmodel.Layer
import com.dhavalbaldha.motionview.viewmodel.TextLayer
import com.dhavalbaldha.motionview_android.databinding.ActivityMainBinding
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import java.util.*

class MainActivity : AppCompatActivity(), TextEditorDialog.OnTextLayerCallback {

    private lateinit var binding: ActivityMainBinding
    private var fontProvider: FontProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        fontProvider = FontProvider(resources, fonts())
        binding.motionView.setMotionViewCallback(motionViewCallback)
    }

    private val motionViewCallback: MotionView.MotionViewCallback =
        object : MotionView.MotionViewCallback {
            override fun onEntitySelected(@Nullable entity: MotionEntity?) {
                if (entity is TextEntity) {
                    binding.lyTextEditPanel.visibility = View.VISIBLE
                } else {
                    binding.lyTextEditPanel.visibility = View.GONE
                }
            }

            override fun onEntityDoubleTap(entity: MotionEntity) {
                startTextEntityEditing()
            }
        }

    fun onClick(view: View) {
        when (view) {
            binding.btnDecreaseTextSize -> decreaseTextEntitySize()
            binding.btnIncreaseTextSize -> increaseTextEntitySize()
            binding.btnChangeColor -> changeTextEntityColor()
            binding.btnChangeFont -> changeTextEntityFont()
            binding.btnEditText -> startTextEntityEditing()
            binding.btnAddSticker -> addSticker(R.drawable.pikachu_2)
            binding.btnAddText -> addTextSticker()
        }
    }

    private fun startTextEntityEditing() {
        val textEntity = currentTextEntity()
        if (textEntity != null) {
            val fragment = TextEditorDialog.getInstance(textEntity.layer.text)
            fragment.show(supportFragmentManager, fragment.javaClass.name)
        }
    }

    private fun increaseTextEntitySize() {
        val textEntity = currentTextEntity()
        if (textEntity != null) {
            textEntity.layer.font.increaseSize(TextLayer.Limits.FONT_SIZE_STEP)
            textEntity.updateEntity()
            binding.motionView.invalidate()
        }
    }

    private fun decreaseTextEntitySize() {
        val textEntity = currentTextEntity()
        if (textEntity != null) {
            textEntity.layer.font.decreaseSize(TextLayer.Limits.FONT_SIZE_STEP)
            textEntity.updateEntity()
            binding.motionView.invalidate()
        }
    }

    private fun currentTextEntity(): TextEntity? {
        return if (binding.motionView.selectedEntity is TextEntity) binding.motionView.selectedEntity as TextEntity else null
    }

    private fun changeTextEntityColor() {
        val textEntity = currentTextEntity() ?: return
        val initialColor: Int = textEntity.layer.font.color
        ColorPickerDialogBuilder
            .with(this@MainActivity)
            .setTitle(getString(R.string.select_color))
            .initialColor(initialColor)
            .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
            .density(8)
            .setPositiveButton(getString(R.string.ok)
            ) { dialog, selectedColor, allColors ->
                textEntity.layer.font.color = selectedColor
                textEntity.updateEntity()
                binding.motionView.invalidate()
            }
            .setNegativeButton(R.string.cancel, null)
            .build()
            .show()
    }

    private fun changeTextEntityFont() {
        val fonts = fontProvider!!.fontNames
        val fontsAdapter = FontsAdapter(this, fonts, fontProvider)
        AlertDialog.Builder(this)
            .setTitle(R.string.select_font)
            .setAdapter(fontsAdapter) { dialogInterface, which ->
                val textEntity = currentTextEntity()
                if (textEntity != null) {
                    textEntity.layer.font.typeface = fonts[which]
                    textEntity.updateEntity()
                    binding.motionView.invalidate()
                }
            }.show()
    }

    private fun addTextSticker() {
        if (fontProvider == null) return
        val textLayer = createTextLayer()
        val textEntity = TextEntity(textLayer,
            binding.motionView.width,
            binding.motionView.height,
            fontProvider!!)
        binding.motionView.addEntityAndPosition(textEntity)

        // move text sticker up so that its not hidden under keyboard
        val center = textEntity.absoluteCenter()
        center.y = center.y * 0.5f
        textEntity.moveCenterTo(center)

        // redraw
        binding.motionView.invalidate()
        startTextEntityEditing()
    }

    private fun createTextLayer(): TextLayer {
        val textLayer = TextLayer()
        val font = Font()
        font.color = TextLayer.Limits.INITIAL_FONT_COLOR
        font.size = TextLayer.Limits.INITIAL_FONT_SIZE
        font.typeface = fontProvider?.defaultFontName
        textLayer.font = font
        textLayer.text = getString(R.string.app_name)
        return textLayer
    }

    override fun textChanged(@NonNull text: String) {
        val textEntity = currentTextEntity() ?: return
        val textLayer = textEntity.layer
        if (text != textLayer.text) {
            textLayer.text = text
            textEntity.updateEntity()
            binding.motionView.invalidate()
        }
    }

    private fun addSticker(stickerResId: Int) {
        binding.motionView.post {
            val layer = Layer()
            val pica = BitmapFactory.decodeResource(resources, stickerResId)
            val entity = ImageEntity(
                layer, pica,
                binding.motionView.width,
                binding.motionView.height)
            binding.motionView.addEntityAndPosition(entity)
        }
    }

    private fun fonts(): HashMap<String, String> {
        val map = HashMap<String, String>()
        map["Arial"] = "Arial.ttf"
        map["Eutemia"] = "Eutemia.ttf"
        map["GREENPIL"] = "GREENPIL.ttf"
        map["Grinched"] = "Grinched.ttf"
        map["Helvetica"] = "Helvetica.ttf"
        map["Libertango"] = "Libertango.ttf"
        map["Metal Macabre"] = "MetalMacabre.ttf"
        map["Parry Hotter"] = "ParryHotter.ttf"
        map["SCRIPTIN"] = "SCRIPTIN.ttf"
        map["The Godfather v2"] = "TheGodfather_v2.ttf"
        map["Aka Dora"] = "akaDora.ttf"
        map["Waltograph"] = "waltograph42.ttf"
        return map
    }
}