package pt.iscde.javadoc.internal.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

public class MethodDom {
	
	private MethodDeclaration methodDeclaration;
	private Map<String, Map<String, String>> javaDocExtract;
	private String signature;

	public MethodDom(MethodDeclaration methodDeclaration) {
		this.methodDeclaration = methodDeclaration;
		this.javaDocExtract = new HashMap<>();
		init();
	}

	private void init() {
		extractSignature();
		extractJavaDoc();
		
	}
	
	private void extractSignature() {
		StringBuilder builder = new StringBuilder();
		
		List<Modifier> modifiers= getMethod().modifiers();
		List<SingleVariableDeclaration> parameters= getMethod().parameters();
		List<Type> throwsExp = getMethod().thrownExceptionTypes();
		
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
		
		if (throwsExp.size() > 0) {
			builder.append(" throws ");
			boolean f = false;
			for (Type type : throwsExp) {
				if (f) {
					builder.append(", ");
				}
				else {
					f = true;
				}
				builder.append(type.toString());
			}
		}
		
		this.signature = builder.toString();
	}

	private Type getReturnType() {
		return getMethod().getReturnType2();
	}

	private void extractJavaDoc() {
		// TODO Auto-generated method stub
		
	}

	public String getName() {
		return getMethod().getName().getFullyQualifiedName();
	}

	private MethodDeclaration getMethod() {
		return this.methodDeclaration;
	}

	public String getSignature() {
		return signature;
	}
}
