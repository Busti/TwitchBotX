package org.foilage.tbx

import java.io.{File, FileInputStream}
import java.nio.file.Files
import java.util

import org.pircbotx.{Configuration, PircBotX}
import org.pircbotx.cap.EnableCapHandler
import org.yaml.snakeyaml.Yaml

object Main {
  var bot: PircBotX = _

  def main(args: Array[String]) {
    val yaml = new Yaml()
    val cfg = yaml.load(new FileInputStream(new File("config.yml"))).asInstanceOf[util.Map[String, Any]]
    val bot1 = cfg.get("bots").asInstanceOf[util.List[util.Map[String, String]]].get(0)

    val config = new Configuration.Builder()
      .setName(bot1.get("name"))
      .setLogin(bot1.get("name"))
      .setServerPassword("oauth:" + bot1.get("oauth"))
      .addServer("irc.twitch.tv")
      .addAutoJoinChannel("#gamesdonequick")
      .setAutoNickChange(false)
      .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
      .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
      .addCapHandler(new EnableCapHandler("twitch.tv/commands"))
      .addListener(new ChatListener)
      .setMessageDelay(1600)
      .buildConfiguration()

    bot = new PircBotX(config)

    new Thread(new ConsoleListener(bot)).start()

    bot.startBot()
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
          running = false
        case _ =>
      }
      Thread.sleep(1000)
    }
  }
}