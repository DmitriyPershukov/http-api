name := "rest http"

version := "0.1"

scalaVersion := "2.13.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"                % "10.1.12",
  "com.typesafe.slick" %% "slick" % "3.3.2",

  "com.typesafe.akka" %% "akka-http-spray-json"     % "10.1.12",
  "com.typesafe.akka" %% "akka-actor-typed"         % "2.6.5",
  "com.typesafe.akka" %% "akka-stream"              % "2.6.5",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  "ch.qos.logback"    % "logback-classic"           % "1.2.3",

  "com.typesafe.akka" %% "akka-http-testkit"        % "10.1.12" % Test,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.6.5"     % Test
)