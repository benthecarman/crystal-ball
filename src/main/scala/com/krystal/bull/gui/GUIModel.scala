package com.krystal.bull.gui

import com.krystal.bull.gui.dialog.AboutDialog
import com.krystal.bull.gui.dialog.AdvancedDialog
import scalafx.beans.property.ObjectProperty
import scalafx.stage.Window

class GUIModel() {

  // Sadly, it is a Java "pattern" to pass null into
  // constructors to signal that you want some default
  val parentWindow: ObjectProperty[Window] =
    ObjectProperty[Window](null.asInstanceOf[Window])

  def onAbout(): Unit = {
    AboutDialog.showAndWait(parentWindow.value)
  }

  def onAdvanced(): Unit = {
    AdvancedDialog.showAndWait(parentWindow.value)
  }

}
