package example

import org.apache.mesos.{Scheduler,MesosSchedulerDriver}
import org.apache.mesos.Protos.{FrameworkInfo,Credential}
import com.google.protobuf.ByteString
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import scala.concurrent.duration._

// inspired by the code futures example framework,
// with heavy modification
object Framework {

  val log = Logger(LoggerFactory.getLogger("framework"))
  val DefaultPrincipal = "example-framework"

  def main(args: Array[String]): Unit = {
    import Resource.{CPU,Memory}

    val master = args.head

    log.info(s"Using the master specified as $master...")

    val scheduler = CustomScheudler(
      desiredInstanceCount = 3,
      container = DockerContainer(
        label = "fedora/apache",
        resources = CPU(1) :: Memory(128) :: Nil
      )
    )

    val principal: String =
      sys.env.get("DEFAULT_PRINCIPAL") getOrElse DefaultPrincipal

    val credential: Option[Credential] = for {
      a <- sys.env.get("MESOS_AUTHENTICATE")
      c <- sys.env.get("DEFAULT_SECRET")
    } yield Credential.newBuilder
      .setPrincipal(principal)
      .setSecret(ByteString.copyFrom(c.getBytes))
      .build

    log.info("booting using a credential? ${credential.isDefined}")

    val frameworkFailoverTimeout = 1.hour.toSeconds // what unit is this? seconds?
    val framework = FrameworkInfo.newBuilder
        .setName("ExampleFramework")
        .setUser("root") // Have Mesos fill in the current user.
        .setFailoverTimeout(frameworkFailoverTimeout)
        .setCheckpoint(sys.env.get("MESOS_CHECKPOINT").map(_.toBoolean).getOrElse(false))
        .setPrincipal(principal)

    val driver =
      credential.map(new MesosSchedulerDriver(scheduler, framework.build, master, _)
        ).getOrElse(new MesosSchedulerDriver(scheduler, framework.build, master))

    // unsafe!
    val status = driver.run()

    log.info(s"resulting status is $status")

  }
}