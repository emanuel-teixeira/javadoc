package pt.iscde.javadoc.internal.model;

import static j2html.TagCreator.body;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.link;
import static j2html.TagCreator.rawHtml;
import static j2html.TagCreator.title;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class JavaDocVisitor extends ASTVisitor {
	
	private StringBuilder javaDocContent;
	
	public JavaDocVisitor() {
		super(true);
		this.javaDocContent = new StringBuilder();
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		Method method = new Method(node);		
		javaDocContent.append(method.toHtmlString());
		return super.visit(node);
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		Entity entity = new Entity(node);
		javaDocContent.append(entity.toHtmlString());
		return super.visit(node);
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	

	public String getHtmlContent() {
		
		return html(head(
		        title("Generated JavaDoc"),
		        link().withRel("stylesheet").withHref("https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css").attr("integrity", "sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO")
		    ),
				body(rawHtml(javaDocContent.toString())).withClasses("bg-dark", "text-white", "container")).render();
	}
}
