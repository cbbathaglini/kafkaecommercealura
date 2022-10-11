package ecommerce;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {
    private final OrderKafkaDispatcher<Order> orderdispatcher = new OrderKafkaDispatcher<Order>();
    private final EmailKafkaDispatcher<Email> emaildispatcher = new EmailKafkaDispatcher<Email>();

    @Override
    public void destroy() {
        super.destroy();
        try{
            orderdispatcher.close();
            emaildispatcher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         try{
                var emailPerson = req.getParameter("email");
                var amount = req.getParameter("amount");
                var orderId = UUID.randomUUID().toString();

                var order = new Order( orderId, new BigDecimal(amount),emailPerson);
                orderdispatcher.send("ECOMMERCE_NEW_ORDER", emailPerson, order);

                var emailvalue = new Email("Processing Order","Thank you for your order! We are processing your order!");
                emaildispatcher.send("ECOMMERCE_SENDEMAIL_NEW_ORDER", emailPerson, emailvalue);

                System.out.println("New order sent successfully");

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println("New order sent");
            } catch (ExecutionException e) {
                throw new ServletException(e);
            } catch (InterruptedException e) {
                throw new ServletException(e);
            }
    }
}
