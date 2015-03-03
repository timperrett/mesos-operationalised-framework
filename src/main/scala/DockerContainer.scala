package example

import org.apache.mesos.Protos.ContainerInfo
import org.apache.mesos.Protos.ContainerInfo.DockerInfo.Network
import Network.{BRIDGE,HOST}
import ContainerInfo.DockerInfo

case class DockerContainer(
  label: String,
  tag: Option[String] = None,
  repository: Option[String] = None,
  networking: Network = HOST,
  resources: Seq[Resource]
){
  val reference =
    repository.map(_ + "/" + label).getOrElse(label) + ":"
    tag.getOrElse("latest")

  val info: ContainerInfo.Builder = {
    val d = DockerInfo.newBuilder
    d.setImage(reference)
    d.setNetwork(networking)

    val c = ContainerInfo.newBuilder
    c.setType(ContainerInfo.Type.DOCKER)
    c.setDocker(d.build)
  }
}
