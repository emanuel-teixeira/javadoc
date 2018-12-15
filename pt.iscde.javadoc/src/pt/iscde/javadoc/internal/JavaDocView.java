package pt.iscde.javadoc.internal;

import java.io.File;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.log.Logger;

import pt.iscde.javadoc.internal.model.JavaDocVisitor;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class JavaDocView implements PidescoView {

	private Logger logger;
	private JavaEditorServices javaEditorServices;

	private Composite viewArea;
	private Map<String, Image> imageMap;
	private Browser browser;
	
	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		this.setViewArea(viewArea);
		this.setImageMap(imageMap);
	
		javaEditorServices = JavaDocServiceFinder.getJavaEditorService();
		
		getViewArea().setLayout(new FillLayout(SWT.VERTICAL));
		
		//Button exportJavaDoc = new Button(viewArea, SWT.VERTICAL);
		//exportJavaDoc.setLayoutData(new RowData(50, 40));
		
		Browser browser = new Browser(viewArea, SWT.VERTICAL);

		this.setBrowser(browser);
		
		//exportJavaDoc.setText("Export JavaDoc to File");

		final JavaDocEditorListener javaDocEditorListener = new JavaDocEditorListener(this);

		if (this.javaEditorServices != null) {
			javaEditorServices.addListener(javaDocEditorListener);
		}
		
		if (javaEditorServices.getOpenedFile() != null) {
			updateJavaDoc(javaEditorServices.getOpenedFile());
		}
	}

	private Browser getBrowser() {
		return browser;
	}

	private void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public Composite getViewArea() {
		return viewArea;
	}

	public void setViewArea(Composite viewArea) {
		this.viewArea = viewArea;
	}

	public Map<String, Image> getImageMap() {
		return imageMap;
	}

	public void setImageMap(Map<String, Image> imageMap) {
		this.imageMap = imageMap;
	}

	public void updateJavaDoc(File file) {
		JavaDocVisitor javaDocVisitor = new JavaDocVisitor();
		this.javaEditorServices.parseFile(file, javaDocVisitor);
		getBrowser().setText(javaDocVisitor.getHtmlContent());
	}

}
