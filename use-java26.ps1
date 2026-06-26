# use-java26.ps1

$env:JAVA_HOME="C:\Program Files\Amazon Corretto\jdk26.0.1_8"
$env:Path="$env:JAVA_HOME\bin;$env:Path"

Write-Host "JAVA_HOME = $env:JAVA_HOME"
java --version