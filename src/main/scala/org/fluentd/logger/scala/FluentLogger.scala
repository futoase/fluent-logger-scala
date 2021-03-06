package org.fluentd.logger.scala

import org.fluentd.logger.scala.sender.Sender
import scala.collection.Map

case class FluentLogger(tag :String, sender: Sender) {

  def log(label: String, key: String, value: Any): Boolean = {
    log(label, key, value, 0)
  }

  def log(label: String, key: String, value: Any, timestamp: Long): Boolean = {
    log(label, Map(key -> value), timestamp)
  }

  def log(label: String, data: Map[String, Any]): Boolean = {
    log(label, data, 0)
  }

  def log(label: String, data: Map[String, Any], timestamp: Long): Boolean = {
    if (timestamp != 0)
      sender.emit(tag + "." + label, timestamp, data.toMap)
    else 
      sender.emit(tag + "." + label, data.toMap)
  }

  def log(label: String, l: Loggable): Boolean = {
    log(label, l.toRecord, 0)
  }

  def log(label: String, l: Loggable, timestamp: Long): Boolean = {
    log(label, l.toRecord, timestamp)
  }

  def flush(): Unit = sender.flush()

  def close(): Unit = {
    flush()
    sender.close()
  }

  def getName: String = sender.getName()

  override def toString: String = sender.toString()

  override def finalize(): Unit = sender.close()

}
