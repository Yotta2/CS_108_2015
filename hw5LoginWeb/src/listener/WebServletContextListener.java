package listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import model.AccountManager;

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
    public void contextInitialized(ServletContextEvent event) {
    	ServletContext servletContext = event.getServletContext();
    	servletContext.setAttribute(AccountManager.class.getName(), new AccountManager());
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event) {
        // TODO Auto-generated method stub
    }
	
}
