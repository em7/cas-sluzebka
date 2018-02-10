# Introduction to source of cas-sluzebka

This project is combined Java/Clojure project. [Leiningen](https://leiningen.org/) is used as a build tool.
Build is platform specific, use platform profile from `project.clj`.

Windows: `lein with-profile +win64 uberjar`

Linux: `lein with-profile +linux-gtk64 uberjar`

Mac: `lein with-profile +macoxs-cocoa64 uberjar`

This create a self-contained executable jar file in `target/uberjar`. Directory `launchers` contains platform specific launch scripts. Rename the jar file to match the name in script and use appropriate launcher.


## Clojure

Logic is written in clojure, see `src/cas_sluzebka/core.clj`. Main entry point is function calc-times.

Since fast start up time is priority, the logic core is loaded dynamically via Clojure API.

## Java

Main entry point is Main. This class holds the main window and initializes the logic in background thread upon startup. Therefore the window is displayed before Clojure is initialized.

The application is not translated yet. Strings are in Czech (utf-8), exception messages in English. All text which is displayed to user is contained in `Main.java` so it will be easy to translate if needed.

## Project file

This application is written using SWT library which is platform specific. There is private repository holding binaries and sources for all platforms in `repository` directory. Leiningen is set up to use this repository.

SWT for other platforms should be installed via similar commands:

``` shell
mvn install:install-file -DgroupId=cas-sluzebka -DartifactId=swt.win64 -Dversion=4.7.2 -Dfile=swt.win64-4.7.2.jar -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=. -DcreateChecksum=true

mvn install:install-file -DgroupId=cas-sluzebka -DartifactId=swt.win64 -Dversion=4.7.2 -Dfile=swt.win64-4.7.2-sources.zip -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=. -DcreateChecksum=true -Dclassifier=sources
```

Official SWT can be downloaded from Eclipse Downloads page. Unofficial builds exist for platforms like FreeBSD/Gtk.

Platform specific profiles need to be activated. From command-line you can use `lein with-profile +win64 repl`. Cursive allows you to set profiles in run configuration dialog. For Emacs, first set variable `M-x set-variable cider-lein-parameters` to `"with-profile +win64 repl :headless :host ::"` (including quotes) then you can use `M-x cider-jack-in`.

For possible profiles consult `project.clj`.

For compiling use appropriate profile as well like `lein with-profile +win64 uberjar`.

## Editing Java sources

If you generate pom file via `lein pom`, the platform-specific dependencies will not be included in the pom file. If you use the generated pom file as base for importing project into your favourite IDE then you have to add SWT and its sources manually into classpath. You do not have to download them separately, they are in this repo in `repository` directory (including sources).

The SWT window was created using Eclipse WindowBuilder.
