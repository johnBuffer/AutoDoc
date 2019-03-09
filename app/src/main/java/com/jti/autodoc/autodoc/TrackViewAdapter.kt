package com.jti.autodoc.autodoc

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TrackViewAdapter(private val tracks : ArrayList<Track>, private val mContext: Context) :
    ArrayAdapter<Track>(mContext, R.layout.activity_main, tracks)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val trackView = inflater.inflate(R.layout.view_track, parent, false)

        val currentTrack = tracks[position]
        val trackName = trackView.findViewById<TextView>(R.id.trackName)
        val trackDaysCount = trackView.findViewById<TextView>(R.id.trackDaysCount)

        trackView.setOnClickListener {
            val intent = Intent(mContext, EditTrackActivity::class.java)
            intent.putExtra("trackData", currentTrack.toString())
            intent.putExtra("trackPosition", position)
            //mContext.startActivity(intent)

            val superActivity = mContext as MainActivity
            superActivity.startEditActivity(intent)
        }

        // Pops a confirmation dialog to remove the current medoc
        trackView.setOnLongClickListener {
            PopUpUtils.getConfirmationPopUp(mContext, "Remove \"" + currentTrack.name + "\" ?",
                {tracks.remove(currentTrack) ; notifyDataSetChanged()},
                {}
            )
            true
        }

        trackName.text = currentTrack.name
        trackDaysCount.text = currentTrack.days.size.toString()

        return trackView
    }
}