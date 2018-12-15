package pt.iscde.javadoc.internal.model;

import static j2html.TagCreator.a;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.h3;
import static j2html.TagCreator.hr;
import static j2html.TagCreator.i;
import static j2html.TagCreator.li;
import static j2html.TagCreator.p;
import static j2html.TagCreator.strong;
import static j2html.TagCreator.ul;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.Type;

import j2html.tags.ContainerTag;

public class Method implements JavaDocHtmlGenerator {

	private SimpleName methodName;
	private Type returnType;
	private Javadoc javaDoc;
	private Block body;
	private List<SingleVariableDeclaration> parameters;
	private List thrownExceptionTypes;
	private List<Modifier> modifiers;

	public Method(MethodDeclaration node) {
		this.setMethodName(node.getName());
		this.setJavaDoc(node.getJavadoc());
		this.setReturnType(node.getReturnType2());
		this.setBody(node.getBody());
		this.setParameters(node.parameters());
		this.setThrownExceptionTypes(node.thrownExceptionTypes());
		this.setModifiers(node.modifiers());
	}

	public SimpleName getMethodName() {
		return methodName;
	}

	public List<?> getParameters() {
		return parameters;
	}

	public void setMethodName(SimpleName simpleName) {
		this.methodName = simpleName;
	}

	public Type getReturnType() {
		return returnType;
	}

	public void setReturnType(Type type) {
		this.returnType = type;
	}

	public Javadoc getJavaDoc() {
		return javaDoc;
	}

	public void setJavaDoc(Javadoc javadoc) {
		this.javaDoc = javadoc;
	}

	public Block getBody() {
		return body;
	}

	public void setBody(Block body) {
		this.body = body;
	}

	public void setParameters(List<SingleVariableDeclaration> parameters) {
		this.parameters = parameters;
	}

	public List<?> getThrownExceptionTypes() {
		return thrownExceptionTypes;
	}

	public void setThrownExceptionTypes(List<?> thrownExceptionTypes) {
		this.thrownExceptionTypes = thrownExceptionTypes;
	}

	public List<Modifier> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<Modifier> modifiers) {
		this.modifiers = modifiers;
	}

	private ContainerTag getCommentsHtml() {

		StringBuilder comments = new StringBuilder();
		StringBuilder returnBuilder = new StringBuilder();
		Map<String, StringBuilder> parameters = new HashMap<String, StringBuilder>();
		Map<String, StringBuilder> mthrows = new HashMap<String, StringBuilder>();

		for (Object javadoc : getJavaDoc().tags()) {
			TagElement te = (TagElement) javadoc;

			if (te.getTagName() == null) {
				for (Object fragments : te.fragments()) {
					comments.append(fragments).append(System.lineSeparator());
				}
			} else if (te.getTagName().equals(TagElement.TAG_PARAM)) {
				SimpleName sn = null;
				for (Object fragments : te.fragments()) {
					if (fragments instanceof SimpleName) {
						sn = ((SimpleName) fragments);
						parameters.put(sn.getFullyQualifiedName(), new StringBuilder());
					} else {
						parameters.get(sn.getFullyQualifiedName()).append(fragments.toString())
								.append(System.lineSeparator());
					}
				}
			} else if (te.getTagName().equals(TagElement.TAG_RETURN)) {
				for (Object fragments : te.fragments()) {
					returnBuilder.append(fragments).append(System.lineSeparator());
				}
			} else if (te.getTagName().equals(TagElement.TAG_THROWS) || te.getTagName().equals(TagElement.TAG_EXCEPTION) ) {
				SimpleName sn = null;
				for (Object fragments : te.fragments()) {
					if (fragments instanceof SimpleName) {
						sn = ((SimpleName) fragments);
						mthrows.put(sn.getFullyQualifiedName(), new StringBuilder());
					} else {
						mthrows.get(sn.getFullyQualifiedName()).append(fragments.toString())
								.append(System.lineSeparator());
					}
				}
			}

		}

		ContainerTag parametersTag = div(p(strong("Parameters: ")),
				ul(each(parameters, e -> li(e.getKey() + " - " + e.getValue().toString()))));

		ContainerTag throwsTag = div(p(strong("Throws: ")),
				ul(each(mthrows, e -> li(e.getKey() + " - " + e.getValue().toString()))));

		ContainerTag returnsTag = div(p(strong("Returns: ")), p(returnBuilder.toString()));

		return div(div(p(i(comments.toString())), parametersTag, returnsTag, throwsTag));

	}

	public ContainerTag getMethodNameHtlm() {

		return div(h3("Method " + getQualifiedName()).withId(getQualifiedName()));
	}

	public ContainerTag getMethodSignatureHtml() {
	
		return div(p(strong(getMethodSignature())));
	}

	public String getQualifiedName() {
		return getMethodName().getFullyQualifiedName();
	}

	@Override
	public String toHtmlString() {
		return div(hr(), div(getMethodNameHtlm()), div(getMethodSignatureHtml()), div(getCommentsHtml())).render();
	}

	public String getMethodSignature() {
		StringBuilder builder = new StringBuilder();

		for (Modifier modifier : modifiers) {
			if (builder.length() > 0) {
				builder.append(" ");
			}
			builder.append(modifier.toString());
		}

		builder.append(" ").append(getReturnType().toString());
		builder.append(" (");

		boolean p = false;

		for (SingleVariableDeclaration parameter : parameters) {

			if (p) {
				builder.append(", ");
			} else {
				p = true;
			}

			builder.append(parameter.toString());
		}
		builder.append(")");
		
		return builder.toString();
	}

	public String getId() {
		return "#".concat(getQualifiedName());
	}

}
