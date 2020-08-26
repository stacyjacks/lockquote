package com.kurmakaeva.anastasia.lockquote.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.kurmakaeva.anastasia.lockquote.BuildConfig
import com.kurmakaeva.anastasia.lockquote.R


class GameResultFragment: Fragment(),
    OnDataPass {

    lateinit var dataPass: OnDataPass

    companion object {
        fun newInstance(): GameResultFragment {
            return GameResultFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        activity?.setTitle(R.string.app_name)
        return inflater.inflate(R.layout.fragment_game_result, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.about_item) {
            showInfo() }

        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val password = view.findViewById<TextView>(R.id.passwordFinal)
        val vinylAnimation = view.findViewById<LottieAnimationView>(R.id.vinyl)
        val anotherGo = view.findViewById<Button>(R.id.anotherGoButton)
        val createNewPassword = view.findViewById<Button>(R.id.makeNewPasswordButton)
        val copyPassword = view.findViewById<Button>(R.id.copyButton)

        password.text = passwordString()
        password.setTextIsSelectable(true)

        vinylAnimation.speed = 1.25f

        copyPassword.setOnClickListener {
            val clipboard: ClipboardManager? =
                activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("passwordReady", password.text)
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(requireActivity(), getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
        }

        anotherGo.setOnClickListener {
            activity?.finish()
        }

        createNewPassword.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPass = context as OnDataPass
    }

    override fun onDataPass(data: String) {
        dataPass.onDataPass(data)
    }

    override fun passwordString(): String {
        return dataPass.passwordString()
    }

    override fun selectedLyric(): String {
        return dataPass.selectedLyric()
    }

    private fun showInfo() {
        val dialogTitle = getString(
            R.string.about_title,
            BuildConfig.VERSION_NAME
        )
        val dialogMessage = getString(R.string.about_message)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }
}