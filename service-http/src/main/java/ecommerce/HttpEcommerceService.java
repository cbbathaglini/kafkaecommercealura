package ecommerce;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


//http://localhost:8089/new
//http://localhost:8089/new?email=carine@email.com&amount=3450
//http://localhost:8089/new?email=carine@email.com&amount=8909

public class HttpEcommerceService {

    public static void main(String[] args) throws Exception {
        var server = new Server(8089);
        var context = new ServletContextHandler();
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new NewOrderServlet()),"/new");

        server.setHandler(context);

        server.start();
        server.join();
    }
}
