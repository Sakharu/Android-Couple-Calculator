package com.lescigognes.chatentempsreel.ui.base

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lescigognes.chatentempsreel.R


abstract class BaseActivity : AppCompatActivity() {
    private var progressBar: ProgressBar? = null
    private var dialog: Dialog? = null
    private var alert: ErrorDialog? = null
    private var filter: IntentFilter? = null
    private var leave = false

    /*****************
     **CYCLE DE VIE**
     ***************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filter = IntentFilter()
    }

    //on enleve le receiver chaque fois qu'on sort de l'activité
    override fun onPause() {
        super.onPause()
        unregisterReceivers(receiver)
    }

    //on la remet quand on revient
    override fun onResume() {
        super.onResume()
        registerReceivers(receiver)
    }


    /***********************
    RECEPTIONS D'INTENT
     **********************/

    //receiver des intent
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            doOnReceive(intent)
        }
    }

    //methode qui va être appelé à chaque action reçue
    open fun doOnReceive(intent: Intent) {}

    fun ajouterActionAIntentFiltrer(action: String) = filter?.addAction(action)

    fun addIntentFilters(vararg actions: String) =
            actions.forEach { ajouterActionAIntentFiltrer(it) }


    private fun registerReceivers(broadcastReceiver: BroadcastReceiver) {
        try {
            registerReceiver(broadcastReceiver, filter)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    private fun unregisterReceivers(broadcastReceiver: BroadcastReceiver) {
        try {
            unregisterReceiver(broadcastReceiver)
        } catch (e: IllegalArgumentException) {
        }
    }


    /***************************
     * CHARGEMENT DE DONNEES
     **************************/

    //fonction qui va permettre d'afficher une barre de progression sans bloquer l'UI le temps de la récupération de toutes les données
    fun showTopProgressBar(layout: ViewGroup) {
        progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        progressBar!!.isIndeterminate = true
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        layout.addView(progressBar, params)
        progressBar!!.visibility = View.VISIBLE
    }

    //fonction qui va permettre de cacher la progressbar précédemment montré
    fun hideProgressBar() {
        progressBar?.animation?.cancel()
        progressBar?.animation = null
        progressBar?.visibility = View.GONE
    }


    //fonction qui va permettre de montrer un spinner de chargement qui va bloquer toutes les interactions avec l'activité en attendant de charger les données
    fun showProgressDialog() {
        if (dialog == null) {
            dialog = Dialog(this)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(false)
            dialog!!.setContentView(R.layout.dialog_progress)
            dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.show()
        }
    }

    fun hideProgressDialog() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }

    fun afficherErreur(erreur: String) {
        if (alert != null) {
            if (alert!!.isVisible()) {
                alert = ErrorDialog()
                alert?.showDialog(this, erreur)
            }
        }
    }

    fun hideKeyboard(activity: Activity) {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog != null)
            dialog!!.dismiss()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (leave)
            finishAffinity()
        else {
            Toast.makeText(this, getString(R.string.willQuit), Toast.LENGTH_SHORT).show()
            leave = true
        }

        Handler().postDelayed({ leave = false }, 200)
    }

}
