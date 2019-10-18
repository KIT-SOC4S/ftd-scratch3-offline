package ftd.ui;

import java.io.IOException;

import ftd.ArduinoCLI;
import ftd.Sb3ToArduinoC;
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
		host.addContext("/convert", new ContextHandler() {
			public int serve(Request req, Response resp) throws IOException {
				resp.getHeaders().add("Content-Type", "text/plain");
				String cSource = null;
				try {
					cSource = Sb3ToArduinoC.convertToArduinoC(req.getBody());
				} catch (Exception e) {
					e.printStackTrace();
					resp.send(200, e.toString());
				}
				if (cSource != null) {
					resp.send(200, cSource);
				}
				return 0;
			}
		}, "POST");
		host.addContext("/compile", new ContextHandler() {
			public int serve(Request req, Response resp) throws IOException {
				resp.getHeaders().add("Content-Type", "text/plain");
				String cSource = null;
				try {
					cSource = ArduinoCLI.compileArduinoC(req.getBody());
				} catch (Exception e) {
					e.printStackTrace();
					resp.send(200, e.toString());
				}
				if (cSource != null) {
					resp.send(200, cSource);
				}
				return 0;
			}
		}, "POST");

		server.start();
	}
}
