package com.ora.scalabeyondbasics

import java.util.concurrent.Executors

import org.scalatest.{FunSpec, Matchers}

class AdvancedImplicitsSpec extends FunSpec with Matchers {
  describe(
    """Implicits is like a Map[Class[A], A] where A is any object and it is tied into the scope,
      | and it is there when you need it, hence it is implicit. This provide a lot of great techniques that we
      | can use in Scala.""".stripMargin) {

    it(
      """is done per scope so in the following example, we will begin with an implicit value
        |  and call it from inside a method which uses a multiple parameter list where one
        |  one group would """.stripMargin) {

      //[Int, 100]
      implicit val rate = 100

      def calc(hours:Int)(implicit rt: Int) = hours * rt

      calc(50) should be (5000)
    }

    it("""will allow you to place something manually, if you want to override the implicit value""".stripMargin) {

      implicit val rate = 100

      def calc(hours:Int)(implicit rt: Int) = hours * rt

      calc(50)(400) should be (20000)
    }

    it(
      """will gripe at compile time if there are two implicit bindings of the same type.  It's
        |  worth noting that what Scala doing are compile time tricks for implicit. One strategy is to
        |  wrap a value in a type to avoid conflict""".stripMargin) {

      case class Rate(x:Int)
      case class Age(x:Int)
      implicit val rate = Rate(100)
      implicit val age = Age(40)

      def calc(hours:Int)(implicit rt: Rate) = hours * rate.x

      calc(50) should be (5000)
    }


    it(
      """is really used to bind services that require something and
        |  you don't particularly need to inject everywhere explicitly, in this
        |  case let's discuss Future[+T]""".stripMargin) {

      import scala.concurrent._

      implicit val executionContext =
        ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))

      val f = Future {
        println(Thread.currentThread().getName)
        Thread.sleep(2000)
        100 + 40
      }

      f.foreach(a => println(s"We got an answer from our Future: $a"))
    }


    it( """can bring up any implicit directly by merely calling up implicitly""") {
      case class IceCream(name:String)
      case class Scoops(num:Int, flavor:IceCream)
      implicit val flavorOfTheDay = IceCream("St. Patty Cabbage")

      def orderIceCream(num:Int)(implicit flavor:IceCream) = Scoops(num, flavor)

      def orderIceCream2(num:Int):Scoops = {
        Scoops(num, implicitly[IceCream])
      }

    }

    it("""the implicit group parameter list, can contain more than one parameter, but
        |  needs to be in the same implicit parameter group""".stripMargin) {

      implicit val bonus = 5000
      implicit val currency = "Euro"

      def calcYearRate(amount:Int)(implicit bonus:Int, currency:String) = {
        amount + bonus + " " + currency
      }

      calcYearRate(60000) should be ("65000 Euro")
    }

    it( """can also be replaced with default parameters, choose accordingly""") {
      def calcYearRate(amount:Int, bonus:Int = 5000, currency:String = "Euro") = {
        amount + bonus + " " + currency
      }

      calcYearRate(60000) should be ("65000 Euro")
    }


    it("""Christopher A. Question: List[String] and List[Double]""") {
      implicit val strings = List("Foo", "Bar", "Baz")
      implicit val doubles = List(1.0, 2.0, 3.0)

      val doublesImpl = implicitly[List[Double]]
      doublesImpl.foldLeft(0.0)(_+_) should be (6.0)
    }


    it("""can be used for something like what Ruby has called
        |  monkey patching or Groovy calls mopping where we can add functionality to
        |  a class that we don't have access to, like isOdd/isEven
        |  in the Int class.  This is what we call implicit wrappers.
        |  First we will use a conversion method.""".stripMargin) {
  
       class IntWrapper(x:Int) {
         def isOdd:Boolean = x % 2 != 0
         def isEven:Boolean = !isOdd
       }

       import scala.language.implicitConversions

       implicit def int2IntWrapper(x:Int):IntWrapper = new IntWrapper(x)

       40.isOdd should be (false)
       25.isOdd should be (true)
       10.isEven should be (true)
    }


    it( """Implicit wrappers can be created using a function and is often easier to mental map.""".stripMargin) {
      class IntWrapper(x:Int) {
        def isOdd:Boolean = x % 2 != 0
        def isEven:Boolean = !isOdd
      }

      import scala.language.implicitConversions

      implicit val int2IntWrapper = (x:Int) => new IntWrapper(x)

      40.isOdd should be (false)
      25.isOdd should be (true)
      10.isEven should be (true)

    }

    it("""can be use a short hand version of this called implicit classes, before using them
        |  there are some rules:
        |  1. They can only be used inside of an object/trait/class
        |  2. They can only take one parameter in the constructor
        |  3. There can not be any colliding method name as that
        |     with the implicit outer scope""".stripMargin) {

      import scala.language.implicitConversions

      implicit class IntWrapper(x:Int) {
        def isOdd:Boolean = x % 2 != 0
        def isEven:Boolean = !isOdd
      }

      40.isOdd should be (false)
      25.isOdd should be (true)
      10.isEven should be (true)
    }

    it("""can also convert things to make it fit into a particular API, this is called implicit conversion,
        | in this scenario we will use a method""".stripMargin) {
       pending
    }

    it("""can also convert things to make it fit into a particular API, this is called implicit conversion,
        | in this scenario we will use a function""".stripMargin) {
       pending
    }

    it( """is done automatically in Scala because what is inside of scala.Predef, for example,
        |  it explains how be can set a scala.Float , and there is java.lang.Float, java primitive float.
        |  We can investigate this by looking at
        |  the documentation.""".stripMargin) {
       pending
    }
  }

  describe("Locating implicits recipes") {
    it( """has a common way, to store that particular implicit
        |  recipe in an object that makes should make
        |  sense and then import that object""".stripMargin) {
       pending
    }

    it( """can also use a companion object to store any implicit recipes""".stripMargin) {
       pending
    }

    it( """can also use a package object to store some of these implicits""") {
       pending
    }

    it("""can use JavaConverters to convert a collection in Java to Scala and vice versa""") {
      pending
    }
  }

  describe("View Bounds are used to ensure that there is a particular recipe for a certain type") {
    it("""Uses <% inside of a parameterized type declaration to determine if there is a conversion available
        | then within you can treat an object as an object of that type. It is unorthodox, and has since been
        | deprecated.""".stripMargin) {
       pending
    }
  }

  describe("""Context Bounds works so that there is a type A, and it requires a B[A] somewhere
      |  within the the implicit scope, for example like Ordered[T], or TypeTag[T], or Numeric[T],
      |  this provides a way to check that something is something can be implicitly defined but
      |  gives the end user no opportunity to the ability to inject a different implementation""".stripMargin) {

    it("""uses the signature [T:WrappedType], which is equivalent to (t:T)(implicit w:WrappedType[T])
          | let's try it with """.stripMargin) {
       pending
    }
  }

  describe(
    """Type Constraints are used to ensure that a particular method can run
      | if a particular generic is of a certain type, this is typically used for
      | one method""".stripMargin) {

    it(
      """uses one operator, =:= which is actually the full type =:=[A,B] that
        |  will to see if something is of the same type""".stripMargin) {
       pending
    }

    it("""uses the operator, <:< which will test if A is a subtype of B""") {
       pending
    }
  }

  describe("Getting around Erasure Using TypeTags") {
    it("used to use Manifest but now uses a type tag to retrieve what is erased") {
      pending
    }
  }

  describe( """Typeclasses are a way of generating or extending behavior using Java-like interfaces,
      |  but operate as outside.  There is another term for this,
      |  and it's called ad-hoc polymorphism""".stripMargin) {

    it( """can be used to determine equality, so whether than make equals inside of an class,
           | it is now an outside concern""".stripMargin) {
       pending
    }

    it("can be used for ordering") {
       pending
    }
  }
}
