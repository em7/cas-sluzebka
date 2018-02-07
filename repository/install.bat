@echo off

REM Installs jars into local repository
REM Requires mvn in path

setlocal

call mvn install:install-file -DgroupId=cas-sluzebka -DartifactId=swt.win64 -Dversion=4.7.2 -Dfile=swt.win64-4.7.2.jar -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=. -DcreateChecksum=true

call mvn install:install-file -DgroupId=cas-sluzebka -DartifactId=swt.win64 -Dversion=4.7.2 -Dfile=swt.win64-4.7.2-sources.zip -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=. -DcreateChecksum=true -Dclassifier=sources

call mvn install:install-file -DgroupId=cas-sluzebka -DartifactId=swt.linux.gtk64 -Dversion=4.7.2 -Dfile=swt.linux.gtk64-4.7.2.jar -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=. -DcreateChecksum=true

call mvn install:install-file -DgroupId=cas-sluzebka -DartifactId=swt.linux.gtk64 -Dversion=4.7.2 -Dfile=swt.linux.gtk64-4.7.2-sources.zip -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=. -DcreateChecksum=true -Dclassifier=sources

endlocal
