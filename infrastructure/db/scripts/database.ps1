param(
    [string]$DBName = "chronos_db"
)

$ErrorActionPreference = "Stop"
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$SqlDir = Join-Path $ScriptDir "sql"

function Show-Banner {
    Clear-Host
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "   Chronos - Database Management Tool" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
}

function Show-Menu {
    Write-Host "[1] Deploy  - Create DB + run all migrations"
    Write-Host "[2] Migrate - Run pending migrations only"
    Write-Host "[3] Seed    - Load seed data"
    Write-Host "[4] Status  - Flyway migration status"
    Write-Host "[5] Clean   - Drop database (DANGER)"
    Write-Host "[0] Exit"
    Write-Host ""
}

function Require-Flyway {
    if (-not (Get-Command flyway -ErrorAction SilentlyContinue)) {
        Write-Host "ERROR: Flyway not found in PATH" -ForegroundColor Red
        exit 1
    }
}

function Invoke-Deploy {
    Require-Flyway
    Write-Host "Creating database (if not exists)..." -ForegroundColor Yellow
    psql -U postgres -d postgres -c "CREATE DATABASE $DBName;" 2>$null

    Write-Host "Running Flyway migrate..." -ForegroundColor Yellow
    flyway migrate
}

function Invoke-Migrate {
    Require-Flyway
    flyway migrate
}

function Invoke-Seed {
    Require-Flyway
    flyway migrate -target=latest
}

function Invoke-Status {
    Require-Flyway
    flyway info
}

function Invoke-Clean {
    Write-Host "WARNING: This will DROP database $DBName" -ForegroundColor Red
    $confirm = Read-Host "Type DROP to continue"
    if ($confirm -ne "DROP") {
        Write-Host "Cancelled"
        return
    }
    psql -U postgres -d postgres -c "DROP DATABASE IF EXISTS $DBName;"
    Write-Host "Database dropped." -ForegroundColor Green
}

# ===== MAIN LOOP =====

do {
    Show-Banner
    Show-Menu
    $choice = Read-Host "Select option"

    switch ($choice) {
        "1" { Invoke-Deploy }
        "2" { Invoke-Migrate }
        "3" { Invoke-Seed }
        "4" { Invoke-Status }
        "5" { Invoke-Clean }
        "0" { break }
        default { Write-Host "Invalid option" -ForegroundColor Red }
    }

    if ($choice -ne "0") {
        Read-Host "Press Enter to continue"
    }

} while ($true)
