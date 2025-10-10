package pt.iade.ei.runupsetup.models

import androidx.annotation.DrawableRes
import java.util.Calendar


data class HistoryItemModel(
    var title : String,
    var date: Calendar,
    var distance: String,
    var duration: String,
    var calories : String,
    var minimumPace : String,
    @DrawableRes var minimap : Int

)
/*
title: String, // nome da atividade
    date: Calendar, // data da atividade
    distance: String, // distância percorrida
    duration: String,  // duração da atividade
    calories: String,  // calorias gastas
    minimumPace: String, // pace médio
    @DrawableRes minimap: Int // mapa da atividade
 */