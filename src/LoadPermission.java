import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoadPermission {

    public static Map<String, String> load() {
        Map<String, String> mapping = new HashMap<>();

        File resultFolder = new File("PScout/results");

        for (File result : Objects.requireNonNull(resultFolder.listFiles())) {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new FileReader(result.getPath() + File.separator + "allmappings"));
                String str, permission = "";
                while ((str = in.readLine()) != null) {
                    if (str.startsWith("Permission:")) {
                        permission = str.replace("Permission:", "");
                    } else {
                        if (str.endsWith(" Callers:")) {
                            continue;
                        }

                        mapping.put(str.substring(0, str.indexOf('>') + 1).trim(), permission);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return mapping;
    }

    public static void main(String[] args) {
        Map<String, String> mapping = load();
        for (String permission: mapping.keySet()) {
            System.out.println(permission);
            System.out.println(mapping.get(permission));
//            if (mapping.get(permission).equals("android.permission.ACCESS_COARSE_LOCATION")){
//                System.out.println(permission);
//                System.out.println(mapping.get(permission));
//            }
        }
    }
}
