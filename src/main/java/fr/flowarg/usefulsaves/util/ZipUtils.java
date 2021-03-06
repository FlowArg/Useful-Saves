package fr.flowarg.usefulsaves.util;

import fr.flowarg.usefulsaves.Main;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author ZeAmateis
 */
public final class ZipUtils
{
    /**
     * Multiple source path to be zipped
     */
    private final List<Path> sourceWhitelist = new ArrayList<>();

    /**
     * Zipped save path
     */
    private Path outputSavePath;

    /**
     * Whether to delete the existing zip file under destination dir with zipName
     */
    private boolean deleteExisting;

    public List<Path> getSourceWhitelist()
    {
        return this.sourceWhitelist;
    }

    public Path getOutputSavePath()
    {
        return this.outputSavePath;
    }

    public void setOutputSavePath(Path outputSavePath)
    {
        this.outputSavePath = outputSavePath;
    }

    public boolean isDeleteExisting()
    {
        return this.deleteExisting;
    }

    public void setDeleteExisting(boolean deleteExisting)
    {
        this.deleteExisting = deleteExisting;
    }

    /**
     * Method to assemble, and zip future save
     *
     * @throws IOException if {@link #createSaveArchive(File)} not work properly
     */
    public void createSave() throws IOException
    {
        final File outputZipSave = outputSavePath.toFile();
        final boolean exists = outputZipSave.exists();

        if (exists && this.deleteExisting && !outputZipSave.delete())
        {
            throw new RuntimeException("cannot delete existing zip file: " + outputZipSave.getAbsolutePath());
        } else if (exists && !this.deleteExisting)
        {
            Main.getPluginLogger().info("Zip file already exists: " + outputZipSave.getAbsolutePath());
            return;
        }
        this.createSaveArchive(outputZipSave);
    }

    /**
     * Create save archive
     */
    private void createSaveArchive(File destination) throws IOException
    {
        if (!this.sourceWhitelist.isEmpty())
        {
            try (ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destination))))
            {
                for (Path sourceDir : this.sourceWhitelist)
                {
                    File sourceDirFile = sourceDir.toFile();
                    if (!sourceDirFile.exists()) throw new RuntimeException("Source dir doesn't exists " + sourceDirFile);

                    addDirRecursively(sourceDirFile.getName(),
                            sourceDirFile.getAbsolutePath(),
                            sourceDirFile,
                            out);
                }
            }
        }
        else Main.getPluginLogger().warn("Whitelist of files to save are empty. Save not created.");
    }

    @NotNull
    private String fileToRelativePath(@NotNull File file, @NotNull String baseDir)
    {
        return file.getAbsolutePath().substring(baseDir.length() + 1);
    }

    private void addDirRecursively(String baseDirName, String baseDir, @NotNull File dirFile, final ZipOutputStream out) throws IOException
    {
        final File[] files = dirFile.listFiles();
        if (files != null)
        {
            for (File file : files)
            {
                if (file.isDirectory())
                {
                    this.addDirRecursively(baseDirName, baseDir, file, out);
                    continue;
                }

                ZipEntry zipEntry = new ZipEntry(baseDirName + File.separatorChar + fileToRelativePath(file, baseDir));
                final BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                zipEntry.setLastModifiedTime(attr.lastModifiedTime());
                zipEntry.setCreationTime(attr.creationTime());
                zipEntry.setLastAccessTime(attr.lastAccessTime());
                zipEntry.setTime(attr.lastModifiedTime().toMillis());

                out.putNextEntry(zipEntry);
                try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file)))
                {
                    byte[] b = new byte[1024];
                    int count;
                    while ((count = in.read(b)) > 0) out.write(b, 0, count);
                    out.closeEntry();
                }
            }
        }
    }
}
