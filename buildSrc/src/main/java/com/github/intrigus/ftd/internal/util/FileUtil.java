package com.github.intrigus.ftd.internal.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

public class FileUtil {
	/**
	 * Deletes a directory including all its files and sub-folders.
	 * 
	 * @param dir the directory to delete
	 * @throws IOException
	 */
	public static void deleteDirectory(Path dir) throws IOException {
		Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * Moves the content of the directory to the specified target directory.
	 * Existing files are replaced.
	 * 
	 * @param source the directory to move
	 * @param target the target directory of the move
	 * @throws IOException
	 */
	public static void moveDirectoryContent(Path source, Path target) throws IOException {
		Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				Path targetPath = target.resolve(source.relativize(dir));
				if (!Files.exists(targetPath)) {
					Files.createDirectory(targetPath);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				deleteDirectory(dir);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.move(file, target.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * Extracts an archive. Tar files are extracted by calling out the external
	 * {@code tar} program. All available java based extractors either don't support
	 * symlinks or don't restore permissions on extraction. All other archive types
	 * are extracted using apache commons compress.
	 * 
	 * @param pathInput  the archive to extract
	 * @param pathOutput the destination, where the files are to be extracted
	 * @throws IOException
	 */
	public static void extract(Path pathInput, Path pathOutput) throws IOException {
		if (pathInput.toFile().getName().endsWith(".tar.bz2")) {
			ProcessResult tarResult = null;
			try {
				tarResult = new ProcessExecutor()
						.command("tar", "-xf", pathInput.toAbsolutePath().toString(), "-C",
								pathOutput.toAbsolutePath().toString())
						.destroyOnExit().exitValueNormal().readOutput(true).executeNoTimeout();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new IOException(e);
			} catch (InvalidExitValueException e) {
				System.out.println(tarResult.outputUTF8());
				throw new RuntimeException(
						"Tar process did not exit with exit value 0. Check StdOut. Exit value: " + e.getExitValue());
			}
		} else {
			Archiver archiver = ArchiverFactory.createArchiver(pathInput.toFile());
			archiver.extract(pathInput.toFile(), pathOutput.toFile());
		}
	}
}
