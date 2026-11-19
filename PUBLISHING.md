# Publishing Aspose.PSD FOSS for Java to Maven Central

Coordinates: `org.aspose:aspose-psd-foss:26.11.0` (the version is stamped only in `pom.xml`).

## Build

```
mvn -B clean verify -Dgpg.skip=true
```

`-Dgpg.skip=true` builds without signing. The pom signs the jars at `verify` with the Aspose FOSS release key, which only
the release workflow holds; without that flag the build stops with `No secret key`.

The build compiles the tests in `test/java` and runs them. A failing test fails the build, and the
release workflow runs the same tests before anything is deployed.

## What is inside the jar

The jar contains this repository's compiled classes only (compiled for Java 11). It bundles no third-party library.
The sources and javadoc jars leave out `java/samples/**`.

## GitHub Release

Each release also attaches the deployed jar, the sources and javadoc jars, their `.asc` signatures
and a `SHA256SUMS` file to the GitHub Release. Maven Central is the source of truth; the attached files are a
convenience copy of the same build.

## Runtime dependencies

None. The library needs only the JDK.

The library uses `java.awt.Color`, so it needs the `java.desktop` module, which every desktop JDK includes.
