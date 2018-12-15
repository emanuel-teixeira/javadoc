package pt.iscde.javadoc.internal;

import java.io.File;

import org.osgi.service.log.Logger;

public class JavaDocEditorListener implements pt.iscte.pidesco.javaeditor.service.JavaEditorListener {

	private JavaDocView parent;
	private final static Logger LOGGER = JavaDocServiceFinder.getFrameworLog().getLogger(JavaDocView.class);
	
	public JavaDocEditorListener(JavaDocView javaDocView) {
		this.parent = javaDocView;
	}
	
	@Override
	public void fileOpened(File file) {
		LOGGER.debug(file.getName() + " was opened in pidesco java editor.");
		this.parent.updateJavaDoc(file);
	}

	@Override
	public void fileSaved(File file) {
		LOGGER.debug(file.getName() + " was saved in pidesco java editor.");
	}

	@Override
	public void fileClosed(File file) {
		LOGGER.debug(file.getName() + " was closed in pidesco java editor.");
	}

	@Override
	public void selectionChanged(File file, String text, int offset, int length) {
		LOGGER.debug(file.getName() + " has selection change in pidesco java editor.");
	}

}
