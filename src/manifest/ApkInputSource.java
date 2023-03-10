package manifest;

import org.xml.sax.InputSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ApkInputSource extends InputSource {
    public ApkInputSource(URL apk) throws IOException {
        this(apk.openStream());
    }

    public ApkInputSource(String apk) throws IOException {
        this(new FileInputStream(apk));
    }

    public ApkInputSource(InputStream apk) throws IOException {
        ZipInputStream zip = new ZipInputStream(apk);
        ZipEntry zipEntry = zip.getNextEntry();
        while (zipEntry != null) {
            if ("AndroidManifest.xml".equals(zipEntry.getName())) {
                setByteStream(zip);
                break;
            }
            zip.closeEntry();
            zipEntry = zip.getNextEntry();
        }
    }
}