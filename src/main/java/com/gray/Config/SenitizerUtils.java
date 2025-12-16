package com.gray.Config;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.stereotype.Component;

@Component
public class SenitizerUtils {

	private final PolicyFactory policyFactory=Sanitizers.FORMATTING
			                                  .and(Sanitizers.BLOCKS)
			                                  .and(Sanitizers.LINKS)
			                                  .and(Sanitizers.STYLES);
	
	public String clean(String input)
	{
		if(input==null)
		{
			return "";
		}
		return policyFactory.sanitize(input);
	}
}
