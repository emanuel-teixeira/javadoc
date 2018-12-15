package pt.iscde.javadoc.internal;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class JavaDocServiceFinder {

	private static JavaDocServiceFinder currentInstance;
	private static BundleContext bundleContext;

	static {
		setCurrentInstance(new JavaDocServiceFinder());
	}

	public static void init(final BundleContext bundleContext) {
		setBundleContext(bundleContext);
	}

	public static void destroy() {
		if (getBundleContext() != null) {
			setBundleContext(null);
		}
	}

	public static JavaDocServiceFinder getInstance() {
		return currentInstance;
	}

	private static void setCurrentInstance(JavaDocServiceFinder currentInstance) {
		JavaDocServiceFinder.currentInstance = currentInstance;
	}

	private static BundleContext getBundleContext() {
		return bundleContext;
	}

	private static void setBundleContext(BundleContext bundleContext) {
		JavaDocServiceFinder.bundleContext = bundleContext;
	}

	public static JavaEditorServices getJavaEditorService() {
		return getService(JavaEditorServices.class);
	}
	
	public static LogService getFrameworLog() {
		return getService(LogService.class);
	}

	public static <T> T getService(final Class<T> clazz) {
		
		T service = null;
		
		if (getBundleContext() != null) {
			ServiceReference<T> serviceReference = getBundleContext().getServiceReference(clazz);
			if (serviceReference != null) {
				service = getBundleContext().getService(serviceReference);
			}

		}
		return service;
	}
}
