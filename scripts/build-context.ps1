#!/usr/bin/env pwsh

param(
    [string]$Output = "context-bundle.md",
    [string]$Root = ".",
    [switch]$IncludeAdditionalMarkdown
)

$ErrorActionPreference = "Stop"

$RootAbs = (Resolve-Path $Root).Path
$OutputPath = Join-Path $RootAbs $Output

function Normalize-PathForCompare {
    param([string]$PathValue)
    return [System.IO.Path]::GetFullPath($PathValue).TrimEnd('\')
}

function Is-ExcludedPath {
    param([string]$FullPath)

    $normalized = Normalize-PathForCompare $FullPath

    return (
    $normalized -like "*\.git\*" -or
            $normalized -like "*\node_modules\*" -or
            $normalized -like "*\target\*" -or
            $normalized -like "*\build\*" -or
            $normalized -like "*\out\*" -or
            $normalized -like "*\dist\*" -or
            $normalized -like "*\.idea\*" -or
            $normalized -like "*\.gradle\*" -or
            $normalized -like "*\coverage\*" -or
            $normalized -like "*\vendor\*"
    )
}

function Resolve-RepoFile {
    param([string]$RelativePath)

    $candidate = Join-Path $RootAbs $RelativePath
    if (Test-Path $candidate -PathType Leaf) {
        return (Resolve-Path $candidate).Path
    }

    return $null
}

function Resolve-RepoFilesFromDirectory {
    param(
        [string]$RelativeDirectory,
        [string]$Filter = "*.md"
    )

    $dir = Join-Path $RootAbs $RelativeDirectory
    if (-not (Test-Path $dir -PathType Container)) {
        return @()
    }

    return Get-ChildItem -Path $dir -File -Filter $Filter |
            Sort-Object Name |
            ForEach-Object { $_.FullName }
}

function Add-SectionHeader {
    param(
        [string]$Path,
        [string]$Title,
        [string]$Description
    )

    @"

## $Title

$Description

"@ | Add-Content -Path $Path
}

function Add-FileToBundle {
    param(
        [string]$BundlePath,
        [string]$FilePath
    )

    $fullPath = (Resolve-Path $FilePath).Path
    $relativePath = $fullPath.Substring($RootAbs.Length).TrimStart('\')

    @"

---
# FILE: $relativePath
---

"@ | Add-Content -Path $BundlePath

    Get-Content -Path $fullPath | Add-Content -Path $BundlePath
}

if (Test-Path $OutputPath) {
    Remove-Item $OutputPath -Force
}

$OutputPathNormalized = Normalize-PathForCompare $OutputPath
$IncludedFiles = New-Object System.Collections.Generic.List[string]
$MissingExpectedFiles = New-Object System.Collections.Generic.List[string]
$IncludedSet = New-Object 'System.Collections.Generic.HashSet[string]' ([System.StringComparer]::OrdinalIgnoreCase)

$CuratedSections = @(
    @{
        Title = "Repository Entry Point"
        Description = "Top-level repository overview and setup guidance."
        Files = @(
            "README.md"
        )
        Directories = @()
    },
    @{
        Title = "Agent Workflow Core"
        Description = "Primary agent onboarding and execution guidance."
        Files = @(
            "docs/agent-start-here.md",
            "docs/agent-development-guide.md",
            "docs/agent-task-template.md",
            "docs/ai_context.md"
        )
        Directories = @()
    },
    @{
        Title = "Project Direction and Domain Context"
        Description = "Core project context, roadmap, domain definitions, and implementation guidance."
        Files = @(
            "docs/vision.md",
            "docs/roadmap.md",
            "docs/implementation-order.md",
            "docs/domain-model.md",
            "docs/data-dictionary.md"
        )
        Directories = @()
    },
    @{
        Title = "Architecture"
        Description = "System architecture, API design, ingestion design, workflow design, and analytics layer."
        Files = @()
        Directories = @(
            "docs/architecture"
        )
    },
    @{
        Title = "Canonical Domain Definitions"
        Description = "Canonical entity specifications that define the domain model for ingestion and read behavior."
        Files = @()
        Directories = @(
            "docs/canonical"
        )
    },
    @{
        Title = "Contracts"
        Description = "Source-to-canonical and other data or behavior mappings."
        Files = @()
        Directories = @(
            "docs/contracts"
        )
    },
    @{
        Title = "Architecture Decision Records"
        Description = "ADRs that capture decisions and constraints agents should respect."
        Files = @()
        Directories = @(
            "docs/adr"
        )
    },
    @{
        Title = "Diagrams"
        Description = "Supplementary diagrams for architecture and system understanding."
        Files = @()
        Directories = @(
            "docs/diagrams"
        )
    },
    @{
        Title = "Dataset Documentation"
        Description = "Dataset-specific documentation that may support ingestion or interpretation."
        Files = @()
        Directories = @(
            "docs/dataset",
            "docs/datasets"
        )
    },
    @{
        Title = "Task-Specific Notes"
        Description = "Task-oriented documentation and work planning notes."
        Files = @()
        Directories = @(
            "docs/tasks"
        )
    }
)

@"
# Context Bundle

Generated: $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
Repository Root: $RootAbs
Mode: Curated$(if ($IncludeAdditionalMarkdown) { " + Additional Markdown" } else { "" })

This bundle is intended for AI agents performing story refinement, implementation, testing, and review.
Files are ordered intentionally to present high-value context first.

---
"@ | Set-Content -Path $OutputPath

foreach ($section in $CuratedSections) {
    $sectionFilesAdded = 0

    foreach ($relativePath in $section.Files) {
        $resolved = Resolve-RepoFile $relativePath

        if ($null -eq $resolved) {
            $MissingExpectedFiles.Add($relativePath)
            continue
        }

        $normalizedResolved = Normalize-PathForCompare $resolved

        if ($normalizedResolved -eq $OutputPathNormalized) {
            continue
        }

        if (Is-ExcludedPath $normalizedResolved) {
            continue
        }

        if ($IncludedSet.Add($normalizedResolved)) {
            if ($sectionFilesAdded -eq 0) {
                Add-SectionHeader -Path $OutputPath -Title $section.Title -Description $section.Description
            }

            Add-FileToBundle -BundlePath $OutputPath -FilePath $normalizedResolved
            $IncludedFiles.Add($normalizedResolved)
            $sectionFilesAdded++
        }
    }

    foreach ($relativeDirectory in $section.Directories) {
        $resolvedFiles = Resolve-RepoFilesFromDirectory -RelativeDirectory $relativeDirectory

        foreach ($file in $resolvedFiles) {
            $normalizedResolved = Normalize-PathForCompare $file

            if ($normalizedResolved -eq $OutputPathNormalized) {
                continue
            }

            if (Is-ExcludedPath $normalizedResolved) {
                continue
            }

            if ($IncludedSet.Add($normalizedResolved)) {
                if ($sectionFilesAdded -eq 0) {
                    Add-SectionHeader -Path $OutputPath -Title $section.Title -Description $section.Description
                }

                Add-FileToBundle -BundlePath $OutputPath -FilePath $normalizedResolved
                $IncludedFiles.Add($normalizedResolved)
                $sectionFilesAdded++
            }
        }
    }
}

if ($IncludeAdditionalMarkdown) {
    Add-SectionHeader `
        -Path $OutputPath `
        -Title "Additional Markdown Files" `
        -Description "Supplementary markdown files discovered automatically, excluding already included files and ignored directories."

    Get-ChildItem -Path $RootAbs -Recurse -File -Include *.md, *.markdown |
            Where-Object {
                -not (Is-ExcludedPath $_.FullName)
            } |
            Sort-Object FullName |
            ForEach-Object {
                $fullPath = (Resolve-Path $_.FullName).Path
                $normalized = Normalize-PathForCompare $fullPath

                if ($normalized -eq $OutputPathNormalized) {
                    return
                }

                if ($IncludedSet.Add($normalized)) {
                    Add-FileToBundle -BundlePath $OutputPath -FilePath $normalized
                    $IncludedFiles.Add($normalized)
                }
            }
}

if ($IncludedFiles.Count -eq 0) {
    Add-Content -Path $OutputPath "`nNo curated markdown files were found."
    Write-Warning "No curated markdown files were found under $RootAbs"
    exit 0
}

if ($MissingExpectedFiles.Count -gt 0) {
    @"

## Missing Expected Files

The following curated files were not found and were skipped:

"@ | Add-Content -Path $OutputPath

    $MissingExpectedFiles |
            Sort-Object -Unique |
            ForEach-Object {
                "- $_" | Add-Content -Path $OutputPath
            }

    Write-Warning "Some curated files were not found. See 'Missing Expected Files' in the bundle."
}

@"

## Bundle Summary

- Curated files included: $($IncludedFiles.Count)
- Missing curated files skipped: $($MissingExpectedFiles | Sort-Object -Unique | Measure-Object | Select-Object -ExpandProperty Count)
- Additional markdown included: $(if ($IncludeAdditionalMarkdown) { "Yes" } else { "No" })

"@ | Add-Content -Path $OutputPath

Write-Host "Context bundle created: $OutputPath"
Write-Host "Curated files included: $($IncludedFiles.Count)"
Write-Host "Missing curated files skipped: $($MissingExpectedFiles | Sort-Object -Unique | Measure-Object | Select-Object -ExpandProperty Count)"
Write-Host "Additional markdown included: $(if ($IncludeAdditionalMarkdown) { 'Yes' } else { 'No' })"