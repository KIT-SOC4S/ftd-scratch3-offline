package ftd;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import ftd.exception.ScratchNoTopLevelBlockException;
import ftd.exception.ScratchParseException;
import ftd.exception.ScratchTooManyTopLevelBlocksException;
import ftd.exception.ScratchUnimplementedException;
import ftd.field.ScratchField;
import ftd.field.ScratchFieldDeserializer;

public class Sb3ToArduinoC {

	public static final String VERSION = "1.0.0";

	public static void main(String[] args) {
		Args parsedArgs = new Args();
		JCommander command = JCommander.newBuilder().addObject(parsedArgs).build();
		command.setProgramName("sb3toc");
		try {
			command.parse(args);
		} catch (ParameterException e) {
			e.usage();
		}
		if (parsedArgs.showVersion) {
			showVersion();
		}
		if (parsedArgs.showHelp) {
			showHelp();
		}

		try {
			System.out.print(convertToArduinoC(System.in));
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(2);
		} catch (ScratchParseException e) {
			e.printStackTrace(System.err);
			System.exit(3);
		} catch (ScratchUnimplementedException e) {
			e.printStackTrace(System.err);
			System.exit(4);
		} catch (ScratchNoTopLevelBlockException e) {
			e.printStackTrace(System.err);
			System.exit(5);
		} catch (ScratchTooManyTopLevelBlocksException e) {
			e.printStackTrace(System.err);
			System.exit(6);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}

		// testRessourceFile(mapper, "biggertest.sb3");
		// testRessourceFile(mapper, "when_input_test.sb3");
		// testRessourceFile(mapper, "motor_stop_test.sb3");
	}

	private static void showHelp() {
		System.out.println("sb3toc takes input from STDIN and writes on sucess to STDOUT");
		System.out.println("Errors are written to STDERR");
		System.out.println("Exit code to error map:");
		System.out.println("1: Unkown error");
		System.out.println("2: I/O error");
		System.out.println("3: Parsing failed");
		System.out.println("4: Unimplemented Scratch feature");
		System.out.println("5: No top-level block i.e. hat found");
		System.out.println("6: More than one top-level block i.e. hat found");
	}

	private static void showVersion() {
		System.out.println("Version: " + VERSION);
	}

	static class Args {
		@Parameter(names = { "-h", "--help", "--usage" }, help = true, description = "Display the help")
		private boolean showHelp;
		@Parameter(names = { "-v", "--version" }, description = "Display the current version")
		private boolean showVersion;
	}

	private static ObjectMapper newDefaultMapper() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(ScratchField.class, new ScratchFieldDeserializer());
		mapper.registerModule(module);
		return mapper;
	}

	public static String convertToArduinoC(InputStream is) throws ScratchParseException, IOException {
		Objects.requireNonNull(is);
		ObjectMapper mapper = newDefaultMapper();

		ZipInputStream zipStream = new ZipInputStream(is);
		byte[] bytes = null;

		ZipEntry entry;

		while ((entry = zipStream.getNextEntry()) != null) {
			if ("project.json".equals(entry.getName())) {
				bytes = zipStream.readAllBytes();
				break;
			} else {
				continue;
			}
		}

		if (bytes == null) {
			throw new RuntimeException("project.json is missing from the .sb3 file.");
		}

		ScratchSave scratchSave;
		try {
			scratchSave = mapper.readValue(bytes, ScratchSave.class);
		} catch (JsonParseException | JsonMappingException e) {
			throw new ScratchParseException(e);
		}

		if (scratchSave == null) {
			throw new RuntimeException("Parsing succeeded, but returned a null value.");
		}

		ScratchBlocks scratchBigger = scratchSave.getBlocks();
		scratchBigger.init();
		String code = scratchBigger.generateCCode();
		return code;
	}
}
