package example

import org.apache.mesos.Protos
import scalaz.Semigroup

trait Resource {
  def build: Protos.Resource.Builder
}
object Resource {

  def fromProto(r: Protos.Resource): Option[Resource] =
    r.getName.trim.toLowerCase match {
      case "cpus" => Option(CPU(r.getScalar.getValue))
      case "mem"  => Option(Memory(r.getScalar.getValue))
      case _      => None
    }

  case class CPU(count: Double) extends Resource {
    def build = {
      Protos.Resource.newBuilder
        .setName("cpus")
        .setType(Protos.Value.Type.SCALAR)
        .setScalar(Protos.Value.Scalar.newBuilder.setValue(count))
    }
  }

  case class Memory(megabytes: Double) extends Resource {
    def build = {
      Protos.Resource.newBuilder
        .setName("mem")
        .setType(Protos.Value.Type.SCALAR)
        .setScalar(Protos.Value.Scalar.newBuilder.setValue(megabytes))
    }
  }

  implicit val ResourceSemigroup: Semigroup[Resource] =
    new Semigroup[Resource] {
      def append(a1: Resource, a2: => Resource) =
        (a1,a2) match {
          case (CPU(a), CPU(b))       => CPU(a+b)
          case (Memory(a), Memory(b)) => Memory(a+b)
          case _ => sys.error("you can only add resources of the same type.")
        }
    }
}
