package com.krystal.bull.gui

import akka.actor.ActorSystem
import com.krystal.bull.gui.settings.Themes
import com.typesafe.config.ConfigFactory
import javafx.scene.paint.Color
import org.bitcoins.core.config._
import org.bitcoins.core.protocol.BitcoinAddress
import org.bitcoins.crypto.AesPassword
import org.bitcoins.dlc.oracle._
import org.bitcoins.dlc.oracle.config.DLCOracleAppConfig
import org.bitcoins.explorer.client.SbExplorerClient
import org.bitcoins.explorer.env.ExplorerEnv
import scalafx.beans.property.{ObjectProperty, StringProperty}

import java.nio.file.{Path, Paths}
import scala.concurrent.ExecutionContextExecutor
import scala.util.Properties

object GlobalData {

  implicit val system: ActorSystem = ActorSystem("krystal-bull")
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  private val DEFAULT_DATADIR: Path = {
    if (Properties.isMac) {
      Paths.get(Properties.userHome,
                "Library",
                "Application Support",
                "Krystal Bull")
    } else if (Properties.isWin) {
      Paths.get("C:",
                "Users",
                Properties.userName,
                "Appdata",
                "Roaming",
                "KrystalBull")
    } else {
      Paths.get(Properties.userHome, ".krystal-bull")
    }
  }

  val oracleNameFile: Path = DEFAULT_DATADIR.resolve("oracleName.txt")

  implicit var appConfig: DLCOracleAppConfig =
    DLCOracleAppConfig.fromDatadir(DEFAULT_DATADIR)

  def setPassword(aesPasswordOpt: Option[AesPassword]): Unit = {
    aesPasswordOpt match {
      case Some(pass) =>
        val overrideConf =
          ConfigFactory.parseString(
            s"bitcoin-s.keymanager.aesPassword = ${pass.toStringSensitive}")
        val newConf = appConfig.newConfigOfType(Vector(overrideConf))
        appConfig = newConf
      case None => ()
    }
  }

  val statusText: StringProperty = StringProperty("")

  val textColor: ObjectProperty[Color] = ObjectProperty(Color.WHITE)

  var darkThemeEnabled: Boolean = true

  def currentStyleSheets: Seq[String] = {
    val loaded = if (GlobalData.darkThemeEnabled) {
      Seq(Themes.DarkTheme.fileLocation)
    } else {
      Seq.empty
    }

    "/themes/base.css" +: loaded
  }

  var oracle: DLCOracle = _

  var advancedMode: Boolean = false

  lazy val stakingAddress: BitcoinAddress = oracle.stakingAddress(MainNet)

  var stakedAmountTextOpt: Option[StringProperty] = None

  var oracleNameOpt: Option[String] = None

  val oracleExplorerClient: SbExplorerClient = SbExplorerClient(
    ExplorerEnv.Production)
}
