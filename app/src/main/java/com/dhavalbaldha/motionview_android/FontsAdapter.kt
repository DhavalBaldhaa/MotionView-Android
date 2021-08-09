package com.dhavalbaldha.motionview_android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.NonNull
import com.dhavalbaldha.motionview.utils.FontProvider
import com.dhavalbaldha.motionview_android.databinding.SimpleListItemBinding

class FontsAdapter(
    context: Context, fontNames: List<String?>,
    private val fontProvider: FontProvider?,
) : ArrayAdapter<String?>(context, 0, fontNames) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    @NonNull
    override fun getView(position: Int, view: View?, @NonNull parent: ViewGroup): View {
        var convertView = view
        val vh: ViewHolder
        if (convertView == null) {
            val binding = SimpleListItemBinding.inflate(inflater, parent, false)
            convertView = binding.root
            vh = ViewHolder(convertView, binding)
            convertView.tag = vh
        } else {
            vh = convertView.tag as ViewHolder
        }
        val fontName = getItem(position)
        vh.binding.text1.typeface = fontProvider?.getTypeface(fontName)
        vh.binding.text1.text = fontName
        return convertView
    }

    private class ViewHolder(rootView: View?, val binding: SimpleListItemBinding)
}