package pt.iscde.javadoc.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.Logger;

public class JavaDocActivator implements BundleActivator {
	
	private static JavaDocActivator currentInstance;
	private BundleContext bundleContext;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		setInstance(this);
		setBundleContext(bundleContext);

		JavaDocServiceFinder.init(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		
		if (currentInstance != null) {
			currentInstance = null;
		}
	}
	
	private void setInstance(JavaDocActivator javaDocActivator) {
		currentInstance = javaDocActivator;
	}
	
	public JavaDocActivator getInstance() {
		return currentInstance;
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

}
