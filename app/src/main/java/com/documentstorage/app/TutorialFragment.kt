package com.documentstorage.app

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView

class TutorialFragment : Fragment() {

    private lateinit var videoView: VideoView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tutorial, container, false)
        videoView = view.findViewById(R.id.videoView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playVideo()
    }

    private fun playVideo() {
        val videoResId = R.raw.demo  // Replace with the resource ID of your video file
        val videoUri = Uri.parse("android.resource://${requireActivity().packageName}/$videoResId")

        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(videoUri)
        videoView.start()
    }
}