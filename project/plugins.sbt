resolvers += Resolver.bintrayIvyRepo("twittercsl", "sbt-plugins")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.1.1")

addSbtPlugin("com.twitter" % "scrooge-sbt-plugin" % "22.12.0")

// The dependency tree plugin isn't really needed but provides an in-browser
// view via dependencyTree and dependencyBrowseTree: it’s bundled with SBT by
// default, and just needs enabling
addDependencyTreePlugin
