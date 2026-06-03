param(
    [string]$DbUrl = $env:RECIPE_DB_URL,
    [string]$DbUser = $env:RECIPE_DB_USER,
    [string]$DbPassword = $env:RECIPE_DB_PASSWORD
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$jarPath = Join-Path $projectRoot "target\RecipeInventoryMVCMySQL-1.0.0.jar"

if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    throw "Java was not found. Install Java 17 or newer, then run this script again."
}

if (-not (Test-Path -LiteralPath $jarPath)) {
    $maven = Get-Command mvn -ErrorAction SilentlyContinue
    if ($maven) {
        Push-Location $projectRoot
        try {
            & mvn clean package
            if ($LASTEXITCODE -ne 0) {
                exit $LASTEXITCODE
            }
        } finally {
            Pop-Location
        }
    } else {
        throw "The runnable jar was not found at $jarPath and Maven is not installed. Install Maven, then run: mvn clean package"
    }
}

$javaArgs = @()
if (-not [string]::IsNullOrWhiteSpace($DbUrl)) {
    $javaArgs += "-Drecipe.db.url=$DbUrl"
}
if (-not [string]::IsNullOrWhiteSpace($DbUser)) {
    $javaArgs += "-Drecipe.db.user=$DbUser"
}
if ($null -ne $DbPassword) {
    $javaArgs += "-Drecipe.db.password=$DbPassword"
}

& java @javaArgs -jar $jarPath
exit $LASTEXITCODE
