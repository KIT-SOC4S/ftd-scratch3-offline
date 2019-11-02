package com.github.intrigus.ftd.ui;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.intrigus.ftd.ArduinoCLI;
import com.github.intrigus.ftd.Sb3ToArduinoC;
import com.github.intrigus.ftd.ui.MessageWrapper.Status;
import com.github.intrigus.ftd.util.ThrowableUtil;

import net.freeutils.httpserver.HTTPServer;
import net.freeutils.httpserver.HTTPServer.ContextHandler;
import net.freeutils.httpserver.HTTPServer.Request;
import net.freeutils.httpserver.HTTPServer.Response;
import net.freeutils.httpserver.HTTPServer.VirtualHost;

public class Server {

	public static void main(String[] args) throws IOException {
		int port = 8888;
		HTTPServer server = new HTTPServer(port);
		VirtualHost host = server.getVirtualHost(null); // default virtual host

		addConvertHandler(host);
		addCompileHandler(host);
		addUploadHandler(host);

		server.start();
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
						result = ArduinoCLI.uploadArduinoC(compileMessage.getCode(), compileMessage.getSerialPort());
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

	/**
	 * Converts a {@link MessageWrapper} to json.
	 * 
	 * @param message the message to convert.
	 * @return returns the message converted to json. Returns an invalid json String
	 *         when conversion fails!.
	 */
	private static String toJson(MessageWrapper message) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(message);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "Internal error:\n" + ThrowableUtil.throwableToString(e);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T fromJson(InputStream inputStream, Class<CompileMessageWrapper> class1)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (T) mapper.readValue(inputStream, class1);
	}
}
