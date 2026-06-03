param(
    [switch]$Run
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$jarPath = Join-Path $projectRoot "target\RecipeInventoryMVCMySQL-1.0.0.jar"

Push-Location $projectRoot
try {
    $maven = Get-Command mvn -ErrorAction SilentlyContinue
    if ($maven) {
        & mvn clean package
        if ($LASTEXITCODE -ne 0) {
            exit $LASTEXITCODE
        }
    } else {
        if (-not (Test-Path -LiteralPath $jarPath)) {
            throw "Maven is not installed and the existing shaded jar is missing. Install Maven, then run: mvn clean package"
        }
        if (-not (Get-Command javac -ErrorAction SilentlyContinue)) {
            throw "javac was not found. Install a full Java 17 JDK, not only a JRE."
        }
        if (-not (Get-Command jar -ErrorAction SilentlyContinue)) {
            throw "jar was not found. Install a full Java 17 JDK, not only a JRE."
        }

        $classesDir = Join-Path $projectRoot "target\classes"
        New-Item -ItemType Directory -Force -Path $classesDir | Out-Null

        $sources = Get-ChildItem -Path (Join-Path $projectRoot "src\main\java") -Filter *.java -Recurse
        if ($sources.Count -eq 0) {
            throw "No Java source files were found under src\main\java."
        }

        $sourceList = [System.IO.Path]::GetTempFileName()
        try {
            $sources.FullName | Set-Content -Encoding ASCII -Path $sourceList

            & javac -encoding UTF-8 -cp $jarPath -d $classesDir "@$sourceList"
            if ($LASTEXITCODE -ne 0) {
                exit $LASTEXITCODE
            }
        } finally {
            Remove-Item -LiteralPath $sourceList -Force -ErrorAction SilentlyContinue
        }

        & jar uf $jarPath -C $classesDir com
        if ($LASTEXITCODE -ne 0) {
            exit $LASTEXITCODE
        }
    }

    if ($Run) {
        & (Join-Path $projectRoot "run-app.ps1")
        exit $LASTEXITCODE
    }
} finally {
    Pop-Location
}
