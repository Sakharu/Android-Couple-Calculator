package com.lescigognes.chatentempsreel

import android.app.Application
import com.lescigognes.chatentempsreel.`object`.User

class App : Application()
{
    companion object
    {
        //var currentUser: User? = User("Mario", "https://d3isma7snj3lcx.cloudfront.net/optim/images/photos/30/50/83/17/super-mario-run-jaquette-ME3050831799_2.jpg", "1")
        var currentUser: User? = User("Luigi", "https://i.etsystatic.com/17348963/r/il/88300f/1619546076/il_794xN.1619546076_agva.jpg", "2")
    }
}