package org.foilage.tbx

import org.pircbotx.hooks.events.{BanListEvent, NoticeEvent, OutputEvent, UnknownEvent}
import org.pircbotx.hooks.{Event, ListenerAdapter}
import org.pircbotx.hooks.types.GenericMessageEvent

class ChatListener extends ListenerAdapter {
  override def onGenericMessage(event: GenericMessageEvent) {
    //println(event.getMessage())
  }

  override def onUnknown(event: UnknownEvent) {

    println(event)
  }
}
