package com.datasift.dropwizard.scala.jersey.inject

import java.io.OutputStream
import java.lang.annotation.Annotation
import java.lang.reflect.Type
import javax.ws.rs.NotFoundException
import javax.ws.rs.core.{MultivaluedMap, MediaType}

import com.datasift.dropwizard.scala.jersey.ParameterizedMessageBodyWriter

class OptionMessageBodyWriter
  extends ParameterizedMessageBodyWriter[Option[_]] {

  override def writeTo(option: Option[_],
                       rawType: Class[_],
                       genericType: Type,
                       annotations: Array[Annotation],
                       mediaType: MediaType,
                       httpHeaders: MultivaluedMap[String, AnyRef],
                       entityStream: OutputStream): Unit = option match {
    case None => throw new NotFoundException
    case Some(data) =>
      val klass = data.getClass
      getTypeArgument(genericType, 0).foreach { tpe =>
        getWriter(klass, tpe, annotations, mediaType).foreach {
          _.writeTo(
            data.asInstanceOf[Any],
            klass,
            tpe,
            annotations,
            mediaType,
            httpHeaders,
            entityStream)
        }
      }
  }
}
