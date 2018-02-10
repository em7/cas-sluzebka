(defproject cas-sluzebka "1.0.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 #_[seesaw "1.4.5"]
                 ]

  :repositories [["cas-sluzebka-local" "file:repository"]]
  :java-source-paths ["java"]
  
  :main cas_sluzebka.Main
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :win64 {:dependencies [[cas-sluzebka/swt.win64 "4.7.2"]]}
             :linux-gtk64 {:dependencies [[cas-sluzebka/swt.linux.gtk64 "4.7.2"]]}
             :macosx-cocoa64 {:dependencies [[cas-sluzebka/swt.macosx.cocoa64 "4.7.2"]]}}
  :pom-addition [:properties
                 [:maven.compiler.source "1.8"]
                 [:maven.compiler.target "1.8"]]
  :javac-options ["-target" "1.8" "-source" "1.8" "-encoding" "utf8"])
