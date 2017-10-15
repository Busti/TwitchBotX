package org.foilage.tbx

import java.util.zip.CRC32
import javafx.scene.paint.Paint

import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.types.GenericMessageEvent

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color._
import scalafx.scene.text.Font

class ChatListener(size: Int) extends ListenerAdapter {
  var words = ListBuffer[(String, Int)]()

  override def onGenericMessage(event: GenericMessageEvent) {
    val msg = event.getMessage
    words ++= msg.split(' ').map(s => (s, {val crc = new CRC32; crc.update(s.getBytes); crc.getValue.toInt}))
    if (words.size > size) {
      val offset = words.size - size
      for (i <- 0 until offset)
        words.remove(0)
    }
  }

  def draw(gc: GraphicsContext) {
    val data = words.toArray

    for ((w, i) <- data
      .groupBy(identity)
      .mapValues(_.length)
      .toSeq
      .sortBy(_._2)
      .map(foo => ("%1.2f [%d] -> ".format(foo._2 / data.length.toFloat, foo._2) + foo._1._1, foo._1._2))
      .reverse
      .zipWithIndex) {
      val code = w._2

      gc.fill = rgb(code & 255, (code >> 8) & 255, (code >> 16) & 255).brighter
      gc.font = Font(12)
      gc.fillText(w._1, 1100, i * 12 + 12)
    }

    for (x <- data.indices; y <- data.indices) {
      if (data(x)._1 == data(y)._1) {
        val code = data(x)._2
        gc.fill = rgb(code & 255, (code >> 8) & 255, (code >> 16) & 255).brighter
        gc.fillRect(x * 1, y * 1, 1, 1)
      }
    }
  }
}
