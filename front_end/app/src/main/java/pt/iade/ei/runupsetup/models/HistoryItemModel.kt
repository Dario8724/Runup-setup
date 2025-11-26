package pt.iade.ei.runupsetup.models

import androidx.annotation.DrawableRes
import java.util.Calendar
// manter o nome HistoryItemModel1 pq se não o arquivo muda para uma classe
data class HistoryItemModel1(
    var title : String,
    var date: Calendar,
    var distance: String,
    var duration: String,
    var calories : String,
    var minimumPace : String,
    @DrawableRes var minimap : Int
)
