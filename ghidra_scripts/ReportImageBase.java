// Runs before Ghidra auto-analysis and confirms the effective ELF image base.
import ghidra.app.script.GhidraScript;

public class ReportImageBase extends GhidraScript {
    @Override
    public void run() throws Exception {
        println("IMPORT_DIAGNOSTIC_PROGRAM=" + currentProgram.getName());
        println("IMPORT_DIAGNOSTIC_IMAGE_BASE=" + currentProgram.getImageBase());
    }
}
