import org.xmlpull.v1.XmlPullParserException;
import soot.*;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.manifest.ProcessManifest;
import soot.options.Options;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CheckPermission {
    private File apkFile;
    private Set<String> codePermissionSet;
    private Set<String> manifestPermissionSet;
    private Map<String, String> mapping;
    private final String androidJarPath = "/Users/clear/Library/Android/sdk/platforms";

    public CheckPermission(File apkFile) {
        this.apkFile = apkFile;
        this.mapping = LoadPermission.load();
        this.codePermissionSet = new HashSet<>();
        this.manifestPermissionSet = new HashSet<>();
    }

    public void checkCodePermission() {
        G.reset();
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_process_dir(Collections.singletonList(this.apkFile.getAbsolutePath()));
        Options.v().set_android_jars(this.androidJarPath);
        Options.v().set_keep_line_number(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_process_multiple_dex(true);
        Options.v().set_whole_program(true);
        soot.Scene.v().loadNecessaryClasses();

        for (SootClass sootClass : Scene.v().getApplicationClasses()) {
            List<SootMethod> classes = sootClass.getMethods();
            try {
                for (SootMethod method : classes) {
                    if (method.isAbstract() || !method.isConcrete()) {
                        continue;
                    }

                    Body body = method.retrieveActiveBody();

                    if (!method.hasActiveBody()) {
                        continue;
                    }

                    for (Unit unit : body.getUnits()) {
                        Stmt stmt = (Stmt) unit;
                        if (stmt.containsInvokeExpr()) {
                            SootMethod invokeMethod = stmt.getInvokeExpr().getMethod();
                            if (mapping.containsKey(invokeMethod.getSignature().trim())) {
                                this.codePermissionSet.add(mapping.get(invokeMethod.getSignature().trim()));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(sootClass);
            }
        }
    }

    public void checkManifestPermission() {
        try {
            ProcessManifest processManifest = new ProcessManifest(this.apkFile.getPath());
            this.manifestPermissionSet = processManifest.getPermissions();
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getCodePermissionSet() {
        return codePermissionSet;
    }

    public Set<String> getManifestPermissionSet() {
        return manifestPermissionSet;
    }

    public static void main(String[] args) {
        CheckPermission cp = new CheckPermission(new File("test/app.buzz.share.lite.apk"));
        cp.checkCodePermission();
        System.out.println(cp.codePermissionSet);
        cp.checkManifestPermission();
        System.out.println(cp.manifestPermissionSet);
    }
}
