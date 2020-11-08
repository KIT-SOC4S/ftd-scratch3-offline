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
import com.github.intrigus.ftd.serial.SerialDevice;
import com.github.intrigus.ftd.serial.SerialDiscovery;
import com.github.intrigus.ftd.serial.SerialDiscoveryDaemon;
import com.github.intrigus.ftd.ui.MessageWrapper.Status;
import com.github.intrigus.ftd.util.ThrowableUtil;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

public class Server {

	public static void main(String[] args) throws IOException {
		Undertow server = getNewConfiguredServerInstance(8888);
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
	public static Undertow getNewConfiguredServerInstance(int port) throws IOException {
		PathHandler handler = new PathHandler();

		addConvertHandler(handler);
		addCompileHandler(handler);
		addUploadHandler(handler);
		addConnectedFtduinoHandler(handler);
		addScratchFilesHandler(handler);
		addConnectedFtduinoAsyncHandler(handler);

		Undertow server = Undertow.builder().addHttpListener(port, "localhost").setHandler(handler).build();
		return server;
	}

	private static void addScratchFilesHandler(PathHandler handler) throws IOException {
		ResourceManager fileResourceManager = new FileResourceManager(new File("scratch/"));
		ResourceHandler resourceHandler = new ResourceHandler(fileResourceManager);
		handler.addPrefixPath("/scratch/", resourceHandler);
	}

	private static void addConvertHandler(PathHandler handler) {
		handler.addExactPath("/convert", new HttpHandler() {
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception {
				if (exchange.isInIoThread()) {
					exchange.dispatch(this);
					return;
				}
				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
				exchange.getResponseHeaders().put(new HttpString("Access-Control-Allow-Origin"), "*");
				exchange.startBlocking();
				String result = null;
				String errorMessage = null;
				Status status;
				try {
					result = Sb3ToArduinoC.convertProjectJsonToArduinoC(exchange.getInputStream());
					status = Status.SUCCESS;
				} catch (Exception e) {
					e.printStackTrace();
					errorMessage = ThrowableUtil.throwableToString(e);
					status = Status.FAILED;
				}

				exchange.setStatusCode(200);
				exchange.getResponseSender().send(toJson(new AnswerMessageWrapper(status, errorMessage, result)));
				exchange.endExchange();
			}
		});
	}

	private static void addCompileHandler(PathHandler handler) {
		handler.addExactPath("/compile", new HttpHandler() {
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception {
				if (exchange.isInIoThread()) {
					exchange.dispatch(this);
					return;
				}
				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
				exchange.getResponseHeaders().put(new HttpString("Access-Control-Allow-Origin"), "*");
				exchange.startBlocking();

				String result = null;
				String errorMessage = null;
				Status status;
				try {
					result = ArduinoCLI.compileArduinoC(
							Sb3ToArduinoC.convertSingleTargetJsonToArduinoC(exchange.getInputStream()));
					status = Status.SUCCESS;
				} catch (Exception e) {
					e.printStackTrace();
					errorMessage = ThrowableUtil.throwableToString(e);
					status = Status.FAILED;
				}

				exchange.setStatusCode(200);
				exchange.getResponseSender().send(toJson(new AnswerMessageWrapper(status, errorMessage, result)));
				exchange.endExchange();
			}
		});
	}

	private static void addUploadHandler(PathHandler handler) {
		handler.addExactPath("/upload", new HttpHandler() {
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception {
				if (exchange.isInIoThread()) {
					exchange.dispatch(this);
					return;
				}
				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
				exchange.getResponseHeaders().put(new HttpString("Access-Control-Allow-Origin"), "*");
				exchange.startBlocking();

				String result = null;
				String errorMessage = null;
				Status status = Status.FAILED;

				CompileMessageWrapper compileMessage = null;
				try {
					compileMessage = fromJson(exchange.getInputStream(), CompileMessageWrapper.class);
				} catch (IOException e) {
					e.printStackTrace();
					errorMessage = ThrowableUtil.throwableToString(e);
					status = Status.FAILED;
				}
				if (compileMessage != null) {
					try {
						result = ArduinoCLI.uploadArduinoC(
								Sb3ToArduinoC.convertSingleTargetJsonToArduinoC(compileMessage.getCode()),
								compileMessage.getSerialPort());
						status = Status.SUCCESS;
					} catch (Exception e) {
						e.printStackTrace();
						errorMessage = ThrowableUtil.throwableToString(e);
						status = Status.FAILED;
					}
				}

				exchange.setStatusCode(200);
				exchange.getResponseSender().send(toJson(new AnswerMessageWrapper(status, errorMessage, result)));
				exchange.endExchange();
			}
		});
	}

	private static void addConnectedFtduinoHandler(PathHandler handler) {
		handler.addExactPath("/ftduinos", new HttpHandler() {
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception {
				if (exchange.isInIoThread()) {
					exchange.dispatch(this);
					return;
				}
				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
				exchange.getResponseHeaders().put(new HttpString("Access-Control-Allow-Origin"), "*");

				List<SerialDevice> result = null;
				String errorMessage = null;
				Status status = Status.FAILED;

				try {
					result = SerialDiscovery.getConnectedFtduinos();
					status = Status.SUCCESS;
				} catch (Exception e) {
					e.printStackTrace();
					errorMessage = ThrowableUtil.throwableToString(e);
					status = Status.FAILED;
				}

				String jsonResult = toJson(result);
				exchange.setStatusCode(200);
				exchange.getResponseSender().send(toJson(new AnswerMessageWrapper(status, errorMessage, jsonResult)));
				exchange.endExchange();
			}
		});
	}

	private static void addConnectedFtduinoAsyncHandler(PathHandler handler) {
		handler.addExactPath("/ftduinosAsync", new HttpHandler() {
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception {
				if (exchange.isInIoThread()) {
					exchange.dispatch(this);
					return;
				}
				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
				exchange.getResponseHeaders().put(new HttpString("Access-Control-Allow-Origin"), "*");

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
				exchange.setStatusCode(200);
				exchange.getResponseSender().send(toJson(new AnswerMessageWrapper(status, errorMessage, jsonResult)));
				exchange.endExchange();
			}
		});
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
