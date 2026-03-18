param(
    [string]$Output = "context-bundle.md",
    [string]$Root = "."
)

$ErrorActionPreference = "Stop"

# Resolve output path early so we can exclude it if it lives under the repo
$resolvedRoot = Resolve-Path $Root
$outputPath = Join-Path $resolvedRoot $Output

# Folders to exclude from recursive scanning
$excludedDirectoryPatterns = @(
    '\\.git\\',
    '\\node_modules\\',
    '\\target\\',
    '\\build\\',
    '\\out\\',
    '\\dist\\',
    '\\.idea\\',
    '\\.gradle\\'
)

function Is-ExcludedDirectory {
    param(
        [string]$FullName
    )

    foreach ($pattern in $excludedDirectoryPatterns) {
        if ($FullName -match $pattern) {
            return $true
        }
    }

    return $false
}

function Get-RelativePath {
    param(
        [string]$BasePath,
        [string]$FullPath
    )

    $baseUri = New-Object System.Uri(($BasePath.TrimEnd('\') + '\'))
    $fileUri = New-Object System.Uri($FullPath)
    return [System.Uri]::UnescapeDataString($baseUri.MakeRelativeUri($fileUri).ToString()).Replace('/', '\')
}

# Remove prior output file so it does not append forever
if (Test-Path $outputPath) {
    Remove-Item $outputPath -Force
}

# Header
@"
# Context Bundle

Generated: $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
Repository Root: $resolvedRoot

---
"@ | Set-Content $outputPath

# Find all markdown files recursively
$markdownFiles = Get-ChildItem -Path $resolvedRoot -Recurse -File -Include *.md, *.markdown |
    Where-Object {
        $fullName = $_.FullName

        # Exclude generated/output file itself
        if ($fullName -eq $outputPath) {
            return $false
        }

        # Exclude unwanted directories
        if (Is-ExcludedDirectory -FullName $fullName) {
            return $false
        }

        return $true
    } |
    Sort-Object FullName

if (-not $markdownFiles) {
    Add-Content $outputPath "`nNo markdown files found.`n"
    Write-Host "No markdown files found under $resolvedRoot"
    exit 0
}

foreach ($file in $markdownFiles) {
    $relativePath = Get-RelativePath -BasePath $resolvedRoot -FullPath $file.FullName

    Add-Content $outputPath "`n`n---"
    Add-Content $outputPath "`n# FILE: $relativePath"
    Add-Content $outputPath "`n---`n"

    Get-Content $file.FullName | Add-Content $outputPath
}

Write-Host "Context bundle created: $outputPath"
Write-Host "Markdown files included: $($markdownFiles.Count)"