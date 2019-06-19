package org.cbot.thymelefhtml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

public class TestStringTemplateEngine {
	@Test
	public void testGenerateStringEngine() {
		Context ctx = new Context();
	    ctx.setVariable("msg", "Hello World!!!");
	    
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(stringTemplateResolver());
		var content = templateEngine.process("<p th:text=\"${msg}\">" + 
				"      Hello, Se Song!" + 
				"    </p>", ctx);
		assertEquals(content, "<p>Hello World!!!</p>");
		assertTrue(true);
	}
	
	private ITemplateResolver stringTemplateResolver() {
        final StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}
