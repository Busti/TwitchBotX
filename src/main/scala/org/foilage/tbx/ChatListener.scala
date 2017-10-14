package org.foilage.tbx

import javafx.scene.paint.Paint

import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.types.GenericMessageEvent

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color._
import scalafx.scene.text.Font

class ChatListener(size: Int) extends ListenerAdapter {
  var words = ListBuffer[String]()

  override def onGenericMessage(event: GenericMessageEvent) {
    val msg = event.getMessage
    words += msg//.split(' ')
    if (words.size > size) {
      val offset = words.size - size
      for (i <- 0 until offset)
        words.remove(0)
    }
  }

  def draw(gc: GraphicsContext) {
    val data = words.toArray

    for ((w, i) <- data.zipWithIndex) {
      val code = data(i).hashCode
      gc.fill = rgb(code & 255, (code >> 8) & 255, (code >> 16) & 255)
      gc.font = Font(6)
      gc.fillText(w, 6 * 200, i * 6)
    }

    for (x <- data.indices; y <- data.indices) {
      if (data(x) == data(y)) {
        val code = data(x).hashCode
        gc.fill = rgb(code & 255, (code >> 8) & 255, (code >> 16) & 255)
        gc.fillRect(x * 6, y * 6, 6, 6)
      }
    }
  }
}
