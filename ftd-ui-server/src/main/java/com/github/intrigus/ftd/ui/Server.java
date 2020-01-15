package com.github.intrigus.ftd.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.intrigus.ftd.ArduinoCLI;
import com.github.intrigus.ftd.Sb3ToArduinoC;
import com.github.intrigus.ftd.SerialDevice;
import com.github.intrigus.ftd.SerialDiscoveryDaemon;
import com.github.intrigus.ftd.ui.MessageWrapper.Status;
import com.github.intrigus.ftd.util.ThrowableUtil;

import net.freeutils.httpserver.HTTPServer;
import net.freeutils.httpserver.HTTPServer.ContextHandler;
import net.freeutils.httpserver.HTTPServer.FileContextHandler;
import net.freeutils.httpserver.HTTPServer.Request;
import net.freeutils.httpserver.HTTPServer.Response;
import net.freeutils.httpserver.HTTPServer.VirtualHost;

public class Server {

	public static void main(String[] args) throws IOException {
		HTTPServer server = getNewConfiguredServerInstance(8888);
		server.start();
	}

	/**
	 * Returns a new server ready for serving the scratch web app and providing the
	 * convert, compile, upload and ftduino enumeration function. You then only have
	 * to call #start() on the server. The server will listen on the specified port.
	 * 
	 * @param port the port the server will listen on
	 * @return a new server read for use
	 * @throws IOException
	 */
	public static HTTPServer getNewConfiguredServerInstance(int port) throws IOException {
		HTTPServer server = new HTTPServer(port);
		VirtualHost host = server.getVirtualHost(null); // default virtual host

		addConvertHandler(host);
		addCompileHandler(host);
		addUploadHandler(host);
		addConnectedFtduinoHandler(host);
		addScratchFilesHandler(host);

		return server;
	}

	private static void addScratchFilesHandler(VirtualHost host) throws IOException {
		host.addContext("/scratch/", new FileContextHandler(new File("scratch/")));
	}

	private static void addConvertHandler(VirtualHost host) {
		host.addContext("/convert", new ContextHandler() {
			public int serve(Request req, Response resp) throws IOException {
				System.out.println("/convert");
				resp.getHeaders().add("Content-Type", "text/plain");
				resp.getHeaders().add("Access-Control-Allow-Origin", "*");
				String result = null;
				String errorMessage = null;
				Status status;
				try {
					result = Sb3ToArduinoC.convertJsonToArduinoC(req.getBody());
					status = Status.SUCCESS;
				} catch (Exception e) {
					e.printStackTrace();
					errorMessage = ThrowableUtil.throwableToString(e);
					status = Status.FAILED;
				}

				resp.send(200, toJson(new AnswerMessageWrapper(status, errorMessage, result)));
				return 0;
			}
		}, "POST");
	}

	private static void addCompileHandler(VirtualHost host) {
		host.addContext("/compile", new ContextHandler() {
			public int serve(Request req, Response resp) throws IOException {
				resp.getHeaders().add("Content-Type", "text/plain");
				resp.getHeaders().add("Access-Control-Allow-Origin", "*");
				String result = null;
				String errorMessage = null;
				Status status;
				try {
					result = ArduinoCLI.compileArduinoC(Sb3ToArduinoC.convertJsonToArduinoC(req.getBody()));
					status = Status.SUCCESS;
				} catch (Exception e) {
					e.printStackTrace();
					errorMessage = ThrowableUtil.throwableToString(e);
					status = Status.FAILED;
				}

				resp.send(200, toJson(new AnswerMessageWrapper(status, errorMessage, result)));
				return 0;
			}
		}, "POST");
	}

	private static void addUploadHandler(VirtualHost host) {
		host.addContext("/upload", new ContextHandler() {
			public int serve(Request req, Response resp) throws IOException {
				resp.getHeaders().add("Content-Type", "text/plain");
				resp.getHeaders().add("Access-Control-Allow-Origin", "*");

				String result = null;
				String errorMessage = null;
				Status status = Status.FAILED;

				CompileMessageWrapper compileMessage = null;
				try {
					compileMessage = fromJson(req.getBody(), CompileMessageWrapper.class);
				} catch (IOException e) {
					e.printStackTrace();
					errorMessage = ThrowableUtil.throwableToString(e);
					status = Status.FAILED;
				}
				if (compileMessage != null) {
					try {
						result = ArduinoCLI.uploadArduinoC(
								Sb3ToArduinoC.convertJsonToArduinoC(compileMessage.getCode()),
								compileMessage.getSerialPort());
						status = Status.SUCCESS;
					} catch (Exception e) {
						e.printStackTrace();
						errorMessage = ThrowableUtil.throwableToString(e);
						status = Status.FAILED;
					}
				}

				resp.send(200, toJson(new AnswerMessageWrapper(status, errorMessage, result)));
				return 0;
			}
		}, "POST");
	}

	private static void addConnectedFtduinoHandler(VirtualHost host) {
		host.addContext("/ftduinos", new ContextHandler() {
			public int serve(Request req, Response resp) throws IOException {
				resp.getHeaders().add("Content-Type", "text/plain");
				resp.getHeaders().add("Access-Control-Allow-Origin", "*");

				List<SerialDevice> result = null;
				String errorMessage = null;
				Status status = Status.FAILED;

				try {
					result = SerialDiscoveryDaemon.getConnectedFtduinos();
					status = Status.SUCCESS;
				} catch (Exception e) {
					e.printStackTrace();
					errorMessage = ThrowableUtil.throwableToString(e);
					status = Status.FAILED;
				}

				String jsonResult = toJson(result);
				resp.send(200, toJson(new AnswerMessageWrapper(status, errorMessage, jsonResult)));
				return 0;
			}
		}, "POST");
	}

	/**
	 * Converts an Object to json.
	 * 
	 * @param object the object to convert
	 * @return returns the object converted to json. Returns an invalid json String
	 *         when conversion fails!
	 */
	private static String toJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "Internal error:\n" + ThrowableUtil.throwableToString(e);
		}
	}

	private static <T> T fromJson(InputStream inputStream, Class<T> class1)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (T) mapper.readValue(inputStream, class1);
	}
}
