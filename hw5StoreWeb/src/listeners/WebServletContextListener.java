package listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import model.ProductCatalog;

/**
 * Application Lifecycle Listener implementation class WebServletContextListener
 *
 */
@WebListener
public class WebServletContextListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public WebServletContextListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent e) {
        ServletContext servletContext = e.getServletContext();
        servletContext.setAttribute(ProductCatalog.class.getName(), new ProductCatalog());
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent e) {
        // TODO Auto-generated method stub
    }
	
}
