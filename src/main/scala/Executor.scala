package example

import org.apache.mesos.{Executor,ExecutorDriver}
import org.apache.mesos.Protos.{TaskID,ExecutorInfo,FrameworkInfo,SlaveInfo,TaskInfo}

class CustomExecutor extends Executor {
  def disconnected(x$1: ExecutorDriver): Unit = ???
  def error(x$1: ExecutorDriver,x$2: String): Unit = ???
  def frameworkMessage(x$1: ExecutorDriver,x$2: Array[Byte]): Unit = ???
  def killTask(x$1: ExecutorDriver,x$2: TaskID): Unit = ???
  def launchTask(x$1: ExecutorDriver,x$2: TaskInfo): Unit = ???
  def registered(x$1: ExecutorDriver,x$2: ExecutorInfo,x$3: FrameworkInfo,x$4: SlaveInfo): Unit = ???
  def reregistered(x$1: ExecutorDriver,x$2: SlaveInfo): Unit = ???
  def shutdown(x$1: ExecutorDriver): Unit = ???
}
