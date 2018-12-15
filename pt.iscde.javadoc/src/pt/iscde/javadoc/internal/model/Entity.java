package pt.iscde.javadoc.internal.model;

import static j2html.TagCreator.a;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.i;
import static j2html.TagCreator.join;
import static j2html.TagCreator.li;
import static j2html.TagCreator.p;
import static j2html.TagCreator.rawHtml;
import static j2html.TagCreator.strong;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.th;
import static j2html.TagCreator.tr;
import static j2html.TagCreator.ul;
import static j2html.TagCreator.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import j2html.tags.ContainerTag;

public class Entity implements JavaDocHtmlGenerator {

	private final static String TAG_DESCRIPTION = "DESCRIPTION";
	private TypeDeclaration node;

	public Entity(TypeDeclaration node) {
		setNode(node);
	}

	@Override
	public String toHtmlString() {
		Map<String, List<String>> javaDoc = getJavadoc();
		
		ContainerTag template = div(p(i(getPackage())), h2(getType() + " " + getName()));
		ContainerTag description = div();
		ContainerTag authors = div();
		ContainerTag version = div();
		ContainerTag since = div();
		ContainerTag superClass = div();
		ContainerTag methodSummary = div();
		
		Map<String, Method> methodList = getMethodSummary();
			
		
		if (getSuperClass() != null) {
			superClass = p(i("extends " + getSuperClass()));
		}		
		if (javaDoc.containsKey(TAG_DESCRIPTION)) {
			description = div(each(javaDoc.get(TAG_DESCRIPTION), o -> rawHtml(o)));
		}
		if (javaDoc.containsKey(TagElement.TAG_AUTHOR)) {
			authors = div(p(strong("Authors: ")),ul(each(javaDoc.get(TagElement.TAG_AUTHOR), o -> li(o))));
		}
		if (javaDoc.containsKey(TagElement.TAG_SINCE)) {
			since = div(p(strong("Since: "), each(javaDoc.get(TagElement.TAG_SINCE), o -> rawHtml(o))));
		}
		if (javaDoc.containsKey(TagElement.TAG_VERSION)) {
			version = div(p(strong("Version: "), each(javaDoc.get(TagElement.TAG_VERSION), o -> rawHtml(o))));
		}
		if(methodList.size() > 0) {
			methodSummary = table(
					thead(tr(th("Method Summary"), th("Signature"))),
					tbody(each(methodList, k -> tr(td(a(k.getKey()).attr("href", k.getValue().getId())), td(k.getValue().getMethodSignature()))))
					).withClasses("table", "table-hover");
		}
		
		return join(template, superClass, description, authors, version, since, methodSummary).render();
	}

	private Map<String, Method> getMethodSummary() {
		
		Map<String, Method> m = new HashMap<>();
		
		for (int i = 0; i < getNode().getMethods().length; i++) {
			
			Method method = new Method(getNode().getMethods()[i]);
			m.put(method.getQualifiedName(), method);				
		}
		
		return m;
	}

	private String getSuperClass() {
		Type superClass = getNode().getSuperclassType();
		return (superClass == null ? null : superClass.toString());
	}

	public String getPackage() {

		ASTNode parent = getNode().getParent();

		if (parent instanceof CompilationUnit) {
			if (((CompilationUnit) parent).getPackage() != null) {
				return ((CompilationUnit) parent).getPackage().getName().getFullyQualifiedName();
			}
		}
		return null;
	}
	
	private final Map<String, List<String>> getJavadoc() {
		Javadoc javadoc = getNode().getJavadoc();
		Map<String, List<String>> javaDocBuilder = new HashMap<String, List<String>>();
		
		for (TagElement tag : ((List<TagElement>) javadoc.tags())) {
			if (tag.getTagName() == null) { //comments
				readTagElement(javaDocBuilder, tag, TAG_DESCRIPTION);
			}
			else if(tag.getTagName().equals(TagElement.TAG_AUTHOR)) {
				readTagElement(javaDocBuilder, tag, TagElement.TAG_AUTHOR);
			}
			else if(tag.getTagName().equals(TagElement.TAG_VERSION)) {
				readTagElement(javaDocBuilder, tag, TagElement.TAG_VERSION);
			}
			else if(tag.getTagName().equals(TagElement.TAG_SINCE)) {
				readTagElement(javaDocBuilder, tag, TagElement.TAG_SINCE);
			}
		}
		
		return javaDocBuilder;
	}

	private void readTagElement(Map<String, List<String>> javaDocBuilder, TagElement tag, String tagName) {
		for (TextElement fragments : ((List<TextElement>) tag.fragments())) {
			if (!javaDocBuilder.containsKey(tagName)) {
				javaDocBuilder.put(tagName, new ArrayList<String>());
			}
			javaDocBuilder.get(tagName).add(fragments.getText().trim());
		}
	}

	public String getType() {
		return (getNode().isInterface() ? "Interface" : "Class");
	}

	public String getName() {
		return getNode().getName().getFullyQualifiedName();
	}

	public TypeDeclaration getNode() {
		return node;
	}

	public void setNode(TypeDeclaration node) {
		this.node = node;
	}

}
