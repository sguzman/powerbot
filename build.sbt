/** Name of project */
name := "ScowerBot"

/** Organization */
organization := "initialcommit.io"

/** Project Version */
version := "1.0"

/** Do not download deps every time */
updateOptions := updateOptions.value.withCachedResolution(true)

/** Pleeease don't use the internet when possible... I'm living out of my car */
offline := true

/** Scala version */
//scalaVersion := "2.12.1"

/** Scalac parameters */
//scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

/** Javac parameters */
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

/** Resolver */
resolvers ++= Seq(
  "Search Maven" at "https://repo1.maven.org/maven2/"
)

/** Source Dependencies */
libraryDependencies ++= Seq(
)

/** Make sure to fork on run */
fork in run := true