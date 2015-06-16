package listeners;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import model.ShoppingCart;

/**
 * Application Lifecycle Listener implementation class WebSessionListener
 *
 */
@WebListener
public class WebSessionListener implements HttpSessionListener {

    /**
     * Default constructor. 
     */
    public WebSessionListener() {
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent e) {
    	e.getSession().setAttribute(ShoppingCart.class.getName(), new ShoppingCart());
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent e) {
    }

}
