package com.loskon.noteminimalism3.app.base.extension.view

import android.view.View
import android.widget.ScrollView

fun ScrollView.scrollBottom(view: View) = post { scrollTo(0, view.bottom) }
