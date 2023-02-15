package top.iseason.metaworldeducation.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class FileUtil {

    private static final String EQUIPMENT_DIR = System.getProperty("user.dir") + File.separatorChar + "equipments";

    public static void uploadFileTo(MultipartFile sourceFile, String folder, Integer id) throws IOException {
        String fileName = sourceFile.getOriginalFilename();
        if (fileName == null) throw new IOException("文件名为空");
        File storeFile = new File(EQUIPMENT_DIR + File.separatorChar +
                folder + File.separatorChar +
                id + fileName.substring(fileName.lastIndexOf(".")));
        storeFile.getParentFile().mkdirs();
        sourceFile.transferTo(storeFile);
    }

    public static File findFile(String folder, Integer id) throws IOException {
        String path = EQUIPMENT_DIR + File.separatorChar + folder;
        AtomicReference<File> file = new AtomicReference<>(null);
        File[] files = new File(path).listFiles();
        if (files == null) throw new IOException("文件不存在");
        //并行查找
        Arrays.stream(files)
                .parallel()
                .forEach(it -> {
                            if (file.get() != null) return;
                            String name = it.getName();
                            int i = name.lastIndexOf('.');
                            if (i < 1) return;
                            name = name.substring(0, i);
                            if (name.equals(String.valueOf(id))) {
                                file.set(it);
                            }
                        }
                );
        if (file.get() == null || files.length != 1) throw new IOException("文件不存在!");
        return file.get();
    }
}
