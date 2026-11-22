# R6 Ghidra 12.1.4 ELF image-base workaround

## Files in this overlay

- `.github/workflows/ghidra.yml` — complete replacement for the current workflow.
- `ghidra_scripts/ReportImageBase.java` — new pre-analysis diagnostic script.

Keep the repository's existing `ghidra_scripts/ExportFunctions.java`, `scripts/inspect_eboot.py`, and `scripts/merge_function_reports.py` unchanged. They are not the source of the repeated `GccExceptionAnalyzer` disassembly messages.

## Why this change

Ghidra 12.1.x has a known `GccExceptionAnalyzer` bug for rebased PIE ELF imports. It can apply the image-base adjustment twice to GCC LSDA call-site / landing-pad addresses. The official Ghidra issue documents this symptom and gives the workaround:

```bash
-loader ElfLoader -loader-imagebase 0
```

The workflow now forces the ordinary ELF loader already seen in the prior run and sets the image base to zero, matching the ELF load virtual base recorded for this eboot. It also prints the imported image base before analysis.

The workflow keeps Ghidra 12.1.4, Java 21, GhidraOrbis 1.0156, the 8 GB headless heap, the 18,000-second per-file timeout, and the existing CSV exporter.

## Log improvements

- `analysis/logs/headless-console.log` mirrors the live headless console output into the artifact.
- `analysis/logs/eboot.log` remains Ghidra's own log.
- `analysis/loader-diagnostics.txt` now searches both logs and counts `GccExceptionAnalyzer` messages.
- Merge is skipped if no per-binary CSV exists, rather than creating an empty merged report.

## Install into a duplicate repository

Extract this ZIP into the root of the duplicated repository, preserving the folder paths, and allow it to overwrite `.github/workflows/ghidra.yml`. Add `ghidra_scripts/ReportImageBase.java` alongside the existing `ExportFunctions.java`.

Do not add `DisableGccExceptionAnalyzer.java`; the image-base workaround is preferable because it aims to keep the GCC exception analyzer enabled while correcting its input addresses.
