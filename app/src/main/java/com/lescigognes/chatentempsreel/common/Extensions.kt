package com.lescigognes.chatentempsreel.common

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

fun AppCompatActivity.showLongSnackBar(message: String) =
    Snackbar.make(this.window.decorView.rootView, message, Snackbar.LENGTH_LONG).show()

fun AppCompatActivity.showShortSnackBar(message: String) =
    Snackbar.make(this.window.decorView.rootView, message, Snackbar.LENGTH_SHORT).show()

fun AppCompatActivity.showInfiniteSnackBar(message: String) =
    Snackbar.make(this.window.decorView.rootView, message, Snackbar.LENGTH_INDEFINITE).show()
