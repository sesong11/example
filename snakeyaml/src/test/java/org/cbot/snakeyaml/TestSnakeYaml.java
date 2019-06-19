package org.cbot.snakeyaml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class TestSnakeYaml {

	@Test
	public void testLoadYamlString() {
		Yaml yaml = new Yaml();
	    String expectedYaml = "---\nname: Silenthand Olleander\nrace: Human\ntraits: [ONE_HAND, ONE_EYE]\n";
	    expectedYaml += "contactDetails:\n";
	    expectedYaml += "   - { \n       type: abc,\n       number: 123\n}\n";
	    Map<Object, Object> document = yaml.load(expectedYaml);
	    assertTrue(document.containsKey("contactDetails"));
	    assertEquals(document.get("name"), "Silenthand Olleander");
	    for(var con: (ArrayList) document.get("contactDetails")) {
	    	Map<Object, Object> item = (Map<Object, Object>) con;
	    	assertEquals("abc", item.get("type"));
	    	assertEquals(123, item.get("number"));
	    }
	}
}
