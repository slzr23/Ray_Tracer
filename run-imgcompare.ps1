$ErrorActionPreference = "Stop"
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$classes = Join-Path $scriptDir "imgcompare\imgcompare\target\classes"
if (-not (Test-Path $classes)) {
  Write-Error "Classpath not found: $classes. Build the project or make sure classes exist."
  exit 1
}
# Optional: also include current dir in classpath
$env:CLASSPATH = $classes
& java com.imgcompare.Main @args
