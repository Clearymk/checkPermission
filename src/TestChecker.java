import com.opencsv.CSVWriter;
import util.Database;
import util.SetOP;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestChecker {
    private Database database;
    private String appsPath;
    private String resultPath;

    public TestChecker(String appsPath, String resultPath) {
        this.database = new Database();
        this.appsPath = appsPath;
        this.resultPath = resultPath;
    }

    public void run() {
        String liteAppID = "com.oyo.consumerlite";

        System.out.println("process " + liteAppID + " permission");
        CheckPermission checkPermission = new CheckPermission(new File(this.appsPath + File.separator + liteAppID + File.separator + liteAppID + ".apk"));

        checkPermission.checkCodePermission();
        checkPermission.checkManifestPermission();

        Set<String> codePermission = checkPermission.getCodePermissionSet();
        Set<String> manifestPermission = checkPermission.getManifestPermissionSet();

        List<String[]> permissionDiff = new ArrayList<>();

        Set<String> intersection = SetOP.performIntersection(codePermission, manifestPermission);

        for (String permission : intersection) {
            permissionDiff.add(new String[]{permission, "1", "1"});
        }

        Set<String> codeDiffManifest = SetOP.performDifference(codePermission, manifestPermission);

        for (String permission : codeDiffManifest) {
            permissionDiff.add(new String[]{permission, "1", "0"});
        }

        Set<String> manifestDiffCode = SetOP.performDifference(manifestPermission, codePermission);

        for (String permission : manifestDiffCode) {
            permissionDiff.add(new String[]{permission, "0", "1"});
        }

        File resultFolder = new File(this.resultPath + File.separator + liteAppID);
        resultFolder.mkdir();

        writeCSV(resultPath + File.separator + liteAppID + File.separator + "diff.csv", permissionDiff);

        System.out.println("finish process " + liteAppID + " permission");


        System.out.println("---------------");
    }


    public void writeCSV(String filePath, List<String[]> data) {

        File file = new File(filePath);
        try {
            FileWriter outputfile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputfile);

            writer.writeNext(new String[]{"Permission", "Code", "Manifest"});

            for (String[] line : data) {
                writer.writeNext(line);
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TestChecker("/Volumes/Data/lint_full_compare_old/download/download_file/", "result").run();
    }

}
