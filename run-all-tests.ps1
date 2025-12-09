# Script de test complet du Ray Tracer
# Génère les images de toutes les scènes et compare avec les cibles

$ErrorActionPreference = "Continue"

# Chemins
$projectRoot = $PSScriptRoot
$raytracer = "$projectRoot\raytracer"
$jar = "$raytracer\target\raytracer-1.0.jar"
$scenesPath = "$raytracer\src\main\resources\scenes"
$imgcompare = "$projectRoot\imgcompare\imgcompare\target\classes"

# Vérifier que le JAR existe
if (-not (Test-Path $jar)) {
    Write-Host "ERREUR: JAR non trouvé: $jar" -ForegroundColor Red
    Write-Host "Compilez d'abord le projet avec: javac -d target/classes -sourcepath src/main/java src/main/java/com/raytracer/Main.java"
    exit 1
}

# Fonction pour comparer deux images
function Compare-Images {
    param (
        [string]$generated,
        [string]$reference
    )
    
    if (-not (Test-Path $reference)) {
        Write-Host "    [SKIP] Pas de cible disponible" -ForegroundColor Yellow
        return
    }
    
    if (-not (Test-Path $generated)) {
        Write-Host "    [ERREUR] Image generee non trouvee: $generated" -ForegroundColor Red
        return
    }
    
    # Utiliser imgcompare
    try {
        $process = Start-Process -FilePath "java" -ArgumentList "-cp", $imgcompare, "com.imgcompare.Main", $generated, $reference -Wait -NoNewWindow -PassThru -RedirectStandardOutput "$env:TEMP\imgcompare_out.txt" -RedirectStandardError "$env:TEMP\imgcompare_err.txt"
        $result = Get-Content "$env:TEMP\imgcompare_out.txt" -Raw
        
        if ($result -match "(\d+)") {
            $diffPixels = $matches[1]
            if ([int]$diffPixels -eq 0) {
                Write-Host "    [OK] Images identiques (0 pixels differents)" -ForegroundColor Green
            } else {
                Write-Host "    [DIFF] $diffPixels pixels differents" -ForegroundColor Yellow
            }
        } else {
            Write-Host "    [INFO] Comparaison effectuee" -ForegroundColor Cyan
        }
    } catch {
        Write-Host "    [WARN] Impossible de comparer les images" -ForegroundColor Yellow
    }
}

# Fonction pour traiter une scène
function Process-Scene {
    param (
        [string]$sceneFile,
        [string]$outputName,
        [string]$referencePath
    )
    
    Write-Host ""
    Write-Host "=== $outputName ===" -ForegroundColor Cyan
    
    # Générer l'image
    $output = java -jar $jar $sceneFile 2>&1
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "    [ERREUR] Echec du rendu" -ForegroundColor Red
        return
    }
    
    # Extraire le nom du fichier de sortie du message
    $outputText = $output | Out-String
    if ($outputText -match "Image sauvegard[^\:]+:\s*(.+\.png)") {
        $generatedFile = "$raytracer\$($matches[1].Trim())"
        Write-Host "    Image: $($matches[1].Trim())"
        
        # Comparer avec la référence si elle existe
        if ($referencePath) {
            Compare-Images -generated $generatedFile -reference $referencePath
        } else {
            Write-Host "    [INFO] Pas de comparaison (scene finale)" -ForegroundColor Gray
        }
    } else {
        Write-Host "    Rendu termine"
    }
}

Write-Host "============================================" -ForegroundColor Magenta
Write-Host "     RAY TRACER - TEST COMPLET" -ForegroundColor Magenta
Write-Host "============================================" -ForegroundColor Magenta

# Changer vers le dossier raytracer pour que les images soient générées au bon endroit
Push-Location $raytracer

# ============ JALON 3 ============
Write-Host "`n`n>>> JALON 3 - Formes géométriques <<<" -ForegroundColor Magenta
$jalon3Tests = @("tp31", "tp32", "tp33", "tp34", "tp35")
foreach ($test in $jalon3Tests) {
    Process-Scene -sceneFile "$scenesPath\jalon3\$test.test" `
                  -outputName "Jalon3/$test" `
                  -referencePath "$scenesPath\jalon3\$test.png"
}

# ============ JALON 4 ============
Write-Host "`n`n>>> JALON 4 - Éclairage <<<" -ForegroundColor Magenta
$jalon4Tests = @("tp41-dir", "tp41-point", "tp42-dir", "tp42-point", "tp43", "tp44", "tp45")
foreach ($test in $jalon4Tests) {
    Process-Scene -sceneFile "$scenesPath\jalon4\$test.test" `
                  -outputName "Jalon4/$test" `
                  -referencePath "$scenesPath\jalon4\$test.png"
}

# ============ JALON 5 ============
Write-Host "`n`n>>> JALON 5 - Matériaux <<<" -ForegroundColor Magenta
$jalon5Tests = @("tp51-diffuse", "tp51-specular", "tp52", "tp53", "tp54", "tp55")
foreach ($test in $jalon5Tests) {
    Process-Scene -sceneFile "$scenesPath\jalon5\$test.test" `
                  -outputName "Jalon5/$test" `
                  -referencePath "$scenesPath\jalon5\$test.png"
}

# ============ JALON 6 ============
Write-Host "`n`n>>> JALON 6 - Réflexions <<<" -ForegroundColor Magenta
$jalon6Tests = @("tp61-dir", "tp61", "tp62-1", "tp62-2", "tp62-3", "tp62-4", "tp62-5", "tp63", "tp64")
foreach ($test in $jalon6Tests) {
    Process-Scene -sceneFile "$scenesPath\jalon6\$test.test" `
                  -outputName "Jalon6/$test" `
                  -referencePath "$scenesPath\jalon6\$test.png"
}

# ============ SCÈNES FINALES ============
Write-Host "`n`n>>> SCÈNES FINALES <<<" -ForegroundColor Magenta
Process-Scene -sceneFile "$scenesPath\final.scene" `
              -outputName "Final" `
              -referencePath $null

Process-Scene -sceneFile "$scenesPath\final_avec_bonus.scene" `
              -outputName "Final avec bonus" `
              -referencePath $null

# Retour au dossier initial
Pop-Location

Write-Host "`n`n============================================" -ForegroundColor Magenta
Write-Host "     TESTS TERMINÉS" -ForegroundColor Magenta
Write-Host "============================================" -ForegroundColor Magenta
Write-Host "`nLes images générées sont dans le dossier raytracer/" -ForegroundColor White
