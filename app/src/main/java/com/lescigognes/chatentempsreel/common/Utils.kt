package com.lescigognes.chatentempsreel.common

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class Utils
{
    companion object
    {
        fun changeConstraintProgrammatically(constraintLayout : ConstraintLayout,idView1 : Int, constraintSet1 : Int,idView2 : Int, constraintSet2 : Int,margin:Int)
        {
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(idView1, constraintSet1, idView2,  constraintSet2, margin)
            constraintSet.applyTo(constraintLayout)
        }
    }
}