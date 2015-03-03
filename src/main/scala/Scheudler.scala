package example

import org.slf4j.LoggerFactory
import org.apache.mesos.Scheduler
import org.apache.mesos.SchedulerDriver
import org.apache.mesos.Protos.{
  ExecutorID,OfferID,FrameworkID,MasterInfo,
  Offer,SlaveID,TaskStatus}
import com.typesafe.scalalogging.Logger
import scala.collection.JavaConverters._

object CustomScheudler {
  val log = Logger(LoggerFactory.getLogger("scheduler"))
}

/**
 * For more information on the methods implemented here, please see:
 * https://github.com/apache/mesos/blob/master/src/java/src/org/apache/mesos/Scheduler.java
 */
case class CustomScheudler(
  instanceCount: Int, // number of container instances to launch
  container: DockerContainer // information about the container to launch
) extends Scheduler {
  import CustomScheudler.log

  /**
   * Invoked when the scheduler becomes "disconnected" from the master
   * (e.g., the master fails and another is taking over).
   *
   * @param driver  The driver that was used to run this scheduler.
   *
   */
  def disconnected(driver: SchedulerDriver): Unit =
    log.info(s"scheduler recieved the disconnected message from $driver")

  /**
   * Invoked when there is an unrecoverable error in the scheduler or
   * driver. The driver will be aborted BEFORE invoking this callback.
   *
   * @param driver  The driver that was used to run this scheduler.
   * @param message The error message.
   *
   */
  def error(driver: SchedulerDriver, message: String): Unit =
    log.info(s"error message from $driver with message '$message' ")

  /**
   * Invoked when an executor has exited/terminated. Note that any
   * tasks running will have TASK_LOST status updates automagically
   * generated.
   *
   * @param driver      The driver that was used to run this scheduler.
   * @param executor    The ID of the executor that was lost.
   * @param slave       The ID of the slave that launched the executor.
   * @param status      The exit status of the executor.
   *
   */
  def executorLost(driver: SchedulerDriver, executor: ExecutorID, slave: SlaveID, status: Int): Unit =
    log.info(s"executorLost message from $driver on $slave for executor '$executor' with status '$status' ")

  /**
   * Invoked when an executor sends a message. These messages are best
   * effort; do not expect a framework message to be retransmitted in
   * any reliable fashion.
   *
   * @param driver      The driver that received the message.
   * @param executorId  The ID of the executor that sent the message.
   * @param slaveId     The ID of the slave that launched the executor.
   * @param data        The message payload.
   *
   */
  def frameworkMessage(driver: SchedulerDriver, executor: ExecutorID, slave: SlaveID, data: Array[Byte]): Unit =
    log.info("scheduler recieved a message from executor $executor on slave $slave. data was '$data'")

  /**
   * Invoked when an offer is no longer valid (e.g., the slave was
   * lost or another framework used resources in the offer). If for
   * whatever reason an offer is never rescinded (e.g., dropped
   * message, failing over framework, etc.), a framwork that attempts
   * to launch tasks using an invalid offer will receive TASK_LOST
   * status updats for those tasks (see {@link #resourceOffers}).
   *
   * @param driver  The driver that was used to run this scheduler.
   * @param offerId The ID of the offer that was rescinded.
   *
   */
  def offerRescinded(driver: SchedulerDriver, offer: OfferID): Unit =
    log.warn(s"prevous off of resource was recinded. OfferId = $offer")

  /**
   * Invoked when the scheduler successfully registers with a Mesos
   * master. A unique ID (generated by the master) used for
   * distinguishing this framework from others and MasterInfo
   * with the IP and port of the current master are provided as arguments.
   *
   * @param driver      The scheduler driver that was registered.
   * @param frameworkId The framework ID generated by the master.
   * @param masterInfo  Info about the current master, including IP and port.
   */
  def registered(driver: SchedulerDriver, frameworkId: FrameworkID, masterInfo: MasterInfo): Unit =
    log.info(s"scheduler successfully connected to the mesos master. FrameworkID issued was '$frameworkId'")

  /**
   * Invoked when the scheduler re-registers with a newly elected Mesos master.
   * This is only called when the scheduler has previously been registered.
   * MasterInfo containing the updated information about the elected master
   * is provided as an argument.
   *
   * @param driver      The driver that was re-registered.
   * @param masterInfo  The updated information about the elected master.
   */
  def reregistered(driver: SchedulerDriver, masterInfo: MasterInfo): Unit =
    log.info("new master elected. scheduler reregistered with master.")

  /**
   * Invoked when resources have been offered to this framework. A
   * single offer will only contain resources from a single slave.
   * Resources associated with an offer will not be re-offered to
   * _this_ framework until either (a) this framework has rejected
   * those resources (see {@link org.apache.mesos.SchedulerDriver#launchTasks}) or (b)
   * those resources have been rescinded (see {@link org.apache.mesos.Scheduler#offerRescinded}).
   * Note that resources may be concurrently offered to more than one
   * framework at a time (depending on the allocator being used). In
   * that case, the first framework to launch tasks using those
   * resources will be able to use them while the other frameworks
   * will have those resources rescinded (or if a framework has
   * already launched tasks with those resources then those tasks will
   * fail with a TASK_LOST status and a message saying as much).
   *
   * @param driver  The driver that was used to run this scheduler.
   * @param offers  The resources offered to this framework.
   */
  def resourceOffers(driver: SchedulerDriver, offers: java.util.List[Offer]): Unit = {
    log.info(s"recieved offer of resources: ${offers.asScala.toList.mkString(",")}")
  }

  /**
   * Invoked when a slave has been determined unreachable (e.g.,
   * machine failure, network partition). Most frameworks will need to
   * reschedule any tasks launched on this slave on a new slave.
   *
   * @param driver  The driver that was used to run this scheduler.
   * @param slaveId The ID of the slave that was lost.
   */
  def slaveLost(driver: SchedulerDriver, slave: SlaveID): Unit =
    log.warn(s"slave with id '$slave' was lost")

  /**
   * Invoked when the status of a task has changed (e.g., a slave is
   * lost and so the task is lost, a task finishes and an executor
   * sends a status update saying so, etc). If implicit
   * acknowledgements are being used, then returning from this
   * callback _acknowledges_ receipt of this status update! If for
   * whatever reason the scheduler aborts during this callback (or
   * the process exits) another status update will be delivered (note,
   * however, that this is currently not true if the slave sending the
   * status update is lost/fails during that time). If explicit
   * acknowledgements are in use, the scheduler must acknowledge this
   * status on the driver.
   *
   * @param driver The driver that was used to run this scheduler.
   * @param status The status update, which includes the task ID and status.
   *
   */
  def statusUpdate(driver: SchedulerDriver, status: TaskStatus): Unit = ???
}
