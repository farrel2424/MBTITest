package com.example.mbtitest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode


class MBTIActivity : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 100
    lateinit var sceneView: ArSceneView
    lateinit var placeButton: ExtendedFloatingActionButton
    private lateinit var modelNode: ArModelNode
    private var extraversionScore = 0
    private var sensingScore = 0
    private var thinkingScore = 0
    private var judgingScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mbti)

        extraversionScore = intent.getIntExtra("extraversionScore", 0)
        sensingScore = intent.getIntExtra("sensingScore", 0)
        thinkingScore = intent.getIntExtra("thinkingScore", 0)
        judgingScore = intent.getIntExtra("judgingScore", 0)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }

        sceneView = findViewById(R.id.sceneView)
        placeButton = findViewById(R.id.place)

        placeButton.setOnClickListener {
            placeModel()
        }

        val mbtiType = determineMBTIType(extraversionScore, sensingScore, thinkingScore, judgingScore)
        val modelPath = getModelPathByMBTI(mbtiType)

        modelNode = ArModelNode().apply {
            loadModelGlbAsync(glbFileLocation = modelPath) {
                sceneView.planeRenderer.isVisible = true
            }
            onAnchorChanged = {
                placeButton.isGone
            }
        }
        sceneView.addChild(modelNode)
    }

    private fun determineMBTIType(extraversion: Int, sensing: Int, thinking: Int, judging: Int): String {
        val eOrI = if (extraversion > 50) "E" else "I"
        val sOrN = if (sensing > 50) "S" else "N"
        val tOrF = if (thinking > 50) "T" else "F"
        val jOrP = if (judging > 50) "J" else "P"
        return "$eOrI$sOrN$tOrF$jOrP"
    }

    private fun getModelPathByMBTI(mbtiType: String): String {
        return when (mbtiType) {
            "INTJ" -> "models/dirt.glb"
            "INTP" -> "models/dirt.glb"
            "ENTJ" -> "models/dirt.glb"
            "ENTP" -> "models/dirt.glb"
            "INFJ" -> "models/dirt.glb"
            "INFP" -> "models/dirt.glb"
            "ENFJ" -> "models/dirt.glb"
            "ENFP" -> "models/dirt.glb"
            "ISTJ" -> "models/dirt.glb"
            "ISFJ" -> "models/dirt.glb"
            "ESTJ" -> "models/dirt.glb"
            "ESFJ" -> "models/dirt.glb"
            "ISTP" -> "models/dirt.glb"
            "ISFP" -> "models/dirt.glb"
            "ESTP" -> "models/dirt.glb"
            "ESFP" -> "models/dirt.glb"
            else -> "models/dirt.glb"
        }
    }

    private fun placeModel() {
        modelNode.anchor()
        sceneView.planeRenderer.isVisible = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
