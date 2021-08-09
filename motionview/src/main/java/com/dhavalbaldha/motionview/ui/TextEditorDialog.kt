package com.dhavalbaldha.motionview.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.view.*
import android.view.WindowManager.LayoutParams.MATCH_PARENT
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import com.dhavalbaldha.motionview.databinding.LayoutTextEditorBinding

class TextEditorDialog : DialogFragment() {
    private var _binding: LayoutTextEditorBinding? = null
    private val binding get() = _binding!!
    private var callback: OnTextLayerCallback? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.requestWindowFeature(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = LayoutTextEditorBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        var text: String? = ""
        if (args != null) {
            text = args.getString(ARG_TEXT)
        }

        initWithTextEntity(text)

        binding.editTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                callback?.textChanged(s.toString())
            }
        })

        binding.textEditorRoot.setOnClickListener { dismiss() }
    }

    private fun initWithTextEntity(text: String?) {
        binding.editTextView.setText(text)
        binding.editTextView.post {
            Selection.setSelection(binding.editTextView.text, binding.editTextView.length())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTextLayerCallback) {
            callback = context
        }
    }

    override fun dismiss() {
        super.dismiss()
        // clearing memory on exit, cos manipulating with text uses bitmaps extensively
        // this does not frees memory immediately, but still can help
        System.gc()
        Runtime.getRuntime().gc()
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val window = dialog.window
            if (window != null) {
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.setLayout(MATCH_PARENT, MATCH_PARENT)
                val windowParams = window.attributes
                window.setDimAmount(0.0f)
                window.attributes = windowParams
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.editTextView.post {
            setEditText(true)
            binding.editTextView.requestFocus()
            val ims = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            ims.showSoftInput(binding.editTextView, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun setEditText(gainFocus: Boolean) {
        if (!gainFocus) {
            binding.editTextView.clearFocus()
            binding.editTextView.clearComposingText()
        }
        binding.editTextView.isFocusableInTouchMode = gainFocus
        binding.editTextView.isFocusable = gainFocus
    }

    interface OnTextLayerCallback {
        fun textChanged(text: String)
    }

    companion object {
        const val ARG_TEXT = "editor_text_arg"
        fun getInstance(textValue: String?): TextEditorDialog {
            val fragment = TextEditorDialog()
            val args = Bundle()
            args.putString(ARG_TEXT, textValue)
            fragment.arguments = args
            return fragment
        }
    }
}