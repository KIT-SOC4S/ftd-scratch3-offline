package com.github.intrigus.ftd.internal.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.rauschig.jarchivelib.CompressionType;

import com.github.intrigus.ftd.internal.dev.MyFileModeMapper;

/**
 * This class is not ideal. It works around the problem that NO java based
 * library is able to correctly decompress tar files. They don't support the
 * "LINK" file type of a tar archive. Also I'm not sure whether they would
 * correctly decompress sym links on a Windows host.
 *
 */
public class TarExtractor {

	private CompressionType compressionType;

	public TarExtractor(CompressionType compressionType) {
		this.compressionType = compressionType;
	}

	public void extract(Path pathInput, Path pathOutput) throws IOException {
		try (InputStream is = Files.newInputStream(pathInput);
				CompressorInputStream in = (compressionType == CompressionType.BZIP2)
						? new BZip2CompressorInputStream(is, true)
						: new GzipCompressorInputStream(is, true);
				TarArchiveInputStream debInputStream = new TarArchiveInputStream(in);) {
			Map<String, SymlinkableTarArchiveEntry> entries = new HashMap<>();
			Map<String, SymlinkableTarArchiveEntry> unlinkedEntries = new HashMap<>();
			for (TarArchiveEntry entry = debInputStream.getNextTarEntry(); entry != null; entry = debInputStream
					.getNextTarEntry()) {
				checkZipSlip(pathOutput.toFile(), entry);
				SymlinkableTarArchiveEntry workingEntry = new SymlinkableTarArchiveEntry(entry.getName(), entry,
						debInputStream.readAllBytes(), entry.isLink() || entry.isSymbolicLink());
				if (workingEntry.isLinked) {
					unlinkedEntries.put(entry.getName(), workingEntry);
				}
				entries.put(entry.getName(), workingEntry);
			}

			deduplicateLinks(entries, unlinkedEntries);

			for (Entry<String, SymlinkableTarArchiveEntry> entry : entries.entrySet()) {
				Path pathEntryOutput = pathOutput.resolve(entry.getKey());
				if (entry.getValue().entry.isDirectory()) {
					if (!Files.exists(pathEntryOutput))
						Files.createDirectories(pathEntryOutput);
				} else {
					Files.createDirectories(pathEntryOutput.getParent());
					Files.copy(new ByteArrayInputStream(entry.getValue().data), pathEntryOutput);
				}
				MyFileModeMapper.map(entry.getValue().entry.getMode(), pathEntryOutput);
			}

		}

	}

	private static void deduplicateLinks(Map<String, SymlinkableTarArchiveEntry> entries,
			Map<String, SymlinkableTarArchiveEntry> unlinkedEntries) {
		while (true) {
			Set<Entry<String, SymlinkableTarArchiveEntry>> unlinkedEntriesSet = unlinkedEntries.entrySet();
			int unlinkedEntriesSetSize = unlinkedEntriesSet.size();
			Iterator<Entry<String, SymlinkableTarArchiveEntry>> iter = unlinkedEntriesSet.iterator();
			Entry<String, SymlinkableTarArchiveEntry> entry;
			while (iter.hasNext()) {
				entry = iter.next();
				SymlinkableTarArchiveEntry tarEntry;
				if (entry.getValue().entry.isSymbolicLink()) {
					tarEntry = entries.get(
							Paths.get(entry.getKey()).resolveSibling(entry.getValue().entry.getLinkName()).toString());
				} else {
					tarEntry = entries.get(entry.getValue().entry.getLinkName());
				}
				if (tarEntry != null && !tarEntry.isLinked) {
					entry.getValue().data = tarEntry.data;
					entry.getValue().isLinked = false;
					iter.remove();
				}
			}
			if (unlinkedEntriesSet.isEmpty()) {
				break;
			}
			if (unlinkedEntriesSetSize == unlinkedEntriesSet.size()) {
				System.err.println(unlinkedEntries);
				throw new RuntimeException("Deduplicating symlinks failed. No progress happened in this loop");
			}
		}
	}

	/**
	 * Guards against the zip slip vulnerability. See
	 * <a href="https://github.com/snyk/zip-slip-vulnerability">here</a> for more
	 * information about the zip slip vulnerability.
	 */
	private static void checkZipSlip(File outputDir, TarArchiveEntry entry) throws IOException {
		String canonicalDestinationDirPath = outputDir.getCanonicalPath();
		File destinationfile = new File(outputDir, entry.getName());
		String canonicalDestinationFile = destinationfile.getCanonicalPath();
		if (!canonicalDestinationFile.startsWith(canonicalDestinationDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir (zip slip): " + entry.getName());
		}
	}

	private static class SymlinkableTarArchiveEntry {
		String fileName;
		TarArchiveEntry entry;
		byte[] data;
		boolean isLinked = false;

		public SymlinkableTarArchiveEntry(String fileName, TarArchiveEntry entry, byte[] data, boolean isLinked) {
			this.fileName = fileName;
			this.entry = entry;
			this.data = data;
			this.isLinked = isLinked;
		}

		@Override
		public String toString() {
			return "SymlinkableTarArchiveEntry [fileName=" + fileName + ", linkName=" + entry.getLinkName()
					+ ", isLinked=" + isLinked + "]";
		}

	}
}
