package com.masoudss.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.masoudss.adapter.AudioAdapter
import com.masoudss.databinding.ActivitySelectAudioBinding
import com.masoudss.model.AudioModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class SelectAudioActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectAudioBinding

    private val audioList = ArrayList<AudioModel>()
    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.DATA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectAudioBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initViews()
        loadAudioFiles()
    }

    private fun initViews() {
        binding.audioRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.audioRecyclerView.adapter = AudioAdapter(this@SelectAudioActivity, audioList)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadAudioFiles() {

        doAsync {

            var cursor: Cursor? = null
            try {
                cursor = contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    MediaStore.Audio.Media.DATE_ADDED + " DESC"
                )

                while (cursor!!.moveToNext()) {
                    audioList.add(
                        AudioModel(
                            title = cursor.getString(1),
                            artist = cursor.getString(2),
                            path = cursor.getString(3)
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }

            uiThread {
                binding.audioRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    fun onSelectAudio(audioModel: AudioModel) {
        val intent = Intent()
        intent.putExtra("path", audioModel.path)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
