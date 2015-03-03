package example

import org.apache.mesos.Protos

trait Resource {
  def build: Protos.Resource.Builder
}

object Resources {
  case class CPU(count: Int) extends Resource {
    def build = {
      Protos.Resource.newBuilder
        .setName("cpus")
        .setType(Protos.Value.Type.SCALAR)
        .setScalar(Protos.Value.Scalar.newBuilder.setValue(count))
    }
  }

  case class Memory(megabytes: Int) extends Resource {
    def build = {
      Protos.Resource.newBuilder
        .setName("mem")
        .setType(Protos.Value.Type.SCALAR)
        .setScalar(Protos.Value.Scalar.newBuilder.setValue(megabytes))
    }
  }
}



