package bindgen.rendering
import bindgen.*

import bindgen.Def.Binding
import scala.scalanative.unsafe.Tag
import scala.scalanative.annotation.alwaysinline
import scala.collection.mutable.ListBuffer

def escape(name: String) =
  name match
    case "type"   => "`type`"
    case "val"    => "`val`"
    case "class"  => "`class`"
    case "object" => "`object`"
    case other    => other

case class error(msg: String) extends Exception(msg)

import scala.scalanative.unsafe.*
import scalanative.unsigned.*

def indent(using c: Config): String =
  (" " * (c.indentSize.value * c.indents.value))

def nest(f: Config ?=> Unit)(using config: Config) =
  f(using config.copy(indents = config.indents.map(_ + 1)))

def to(sb: StringBuilder)(using config: Config): Appender =
  line => sb.append(indent(using config) + line + "\n")

def aliasResolver(name: String)(using ar: AliasResolver): CType =
  ar(name)

def packageName(using conf: Config): String = conf.packageName.value

type Appender = Config ?=> String => Unit
type AliasResolver = String => CType
