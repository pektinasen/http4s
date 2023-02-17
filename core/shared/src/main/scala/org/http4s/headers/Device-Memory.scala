/*
 * Copyright 2013 http4s.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.http4s
package headers

import org.http4s.Header
import org.http4s.ParseFailure
import org.typelevel.ci._
import cats.parse.Parser

object `Device-Memory` {

  private val allowed = List("0.25", "0.5", "1", "2", "4", "8")

  def fromString(value: String): ParseResult[`Device-Memory`] = parse(value)
  def unsafeFromString(value: String): `Device-Memory` = fromString(value).fold(throw _, identity)

  private[http4s] def parse(headerValue: String): Either[ParseFailure, `Device-Memory`] =
    (Parser.stringIn(allowed) ~ Parser.end)
      .parse(headerValue) match {
      case Left(_) =>
        ParseResult.fail(
          "Invalid Device-Memory header.",
          s"Value ${headerValue} must be one of ${allowed.mkString(", ")}",
        )
      case Right(_) => ParseResult.success(`Device-Memory`(headerValue))
    }

  implicit val headerInstance: Header[`Device-Memory`, Header.Single] =
    Header.create(
      ci"Device-Memory",
      _.amount,
      parse,
    )

}

final case class `Device-Memory` private(val amount: String) extends AnyVal {
  def copy: Unit = ()
}
