resolvers += Resolver.bintrayIvyRepo("twittercsl", "sbt-plugins")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.1.1")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.3")

addSbtPlugin("com.twitter" % "scrooge-sbt-plugin" % "22.12.0")

// The sbt-dependency-graph plugin isn't really needed but provides an in-browser view via dependencyBrowseTree.
// The terminal-based version dependencyTree does the same thing and is bundled with SBT by default.
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.10.0-RC1")