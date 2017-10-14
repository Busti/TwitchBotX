package org.foilage.tbx

import java.io.{File, FileInputStream}
import java.util

import org.pircbotx.{Configuration, PircBotX}
import org.pircbotx.cap.EnableCapHandler
import org.yaml.snakeyaml.Yaml

import scalafx.Includes._
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.paint.Color._

object Main extends JFXApp {
  var bot: PircBotX = _

  val yaml = new Yaml()
  val cfg = yaml.load(new FileInputStream(new File("config.yml"))).asInstanceOf[util.Map[String, Any]]
  val bot1 = cfg.get("bots").asInstanceOf[util.List[util.Map[String, String]]].get(0)

  val listener = new ChatListener(200)

  val config = new Configuration.Builder()
    .setName(bot1.get("name"))
    .setLogin(bot1.get("name"))
    .setServerPassword("oauth:" + bot1.get("oauth"))
    .addServer("irc.twitch.tv")
    .addAutoJoinChannel("#forsenlol")
    .setAutoNickChange(false)
    .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
    .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
    .addCapHandler(new EnableCapHandler("twitch.tv/commands"))
    .addListener(listener)
    .setMessageDelay(1600)
    .buildConfiguration()

  bot = new PircBotX(config)

  new Thread(new ConsoleListener(bot)).start()

  new Thread(new Runnable {
    override def run() {
      bot.startBot()
    }
  }).start()

  stage = new JFXApp.PrimaryStage {
    title.value = "Twitchgems"
    width = 200
    height = 200
    scene = new Scene { _scene =>
      content = new Canvas {
        width <== _scene.width
        height <== _scene.height
        val timer = AnimationTimer { t =>
          graphicsContext2D.fill = Black
          graphicsContext2D.fillRect(0, 0, width.value, height.value)
          listener.draw(graphicsContext2D)
        }
        timer.start()
      }
    }
  }
}

class ConsoleListener(bot: PircBotX) extends Runnable {
  var running = true

  override def run() {
    while (running) {
      val ln = scala.io.StdIn.readLine()
      ln match {
        case "quit" =>
          bot.send().quitServer()
          Main.stopApp()
          running = false
        case _ =>
      }
      Thread.sleep(1000)
    }
  }
}