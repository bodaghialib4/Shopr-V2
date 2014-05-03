package com.adiguzel.shopr.explanation;

import com.uwetrottmann.shopr.algorithm.LocalizationModule;

public interface ExplanationLocalizer extends LocalizationModule {
	public String getIndifferentToAnyTemplate();
	public String getOnlySomeTemplate();
	public String getAvoidsSomeTemplate();
	public String getPrefersSomeTemplate();
	public String getCommaString();
	public String getAndString();
	
	public String[] getStrongArgumentTemplates();
	public String[] getWeakArgumentTemplates();
	public String[] getSerendipitousityTemplates();
	public String[] getContextArgumentTemplates();
	
	public String getSupportingArgumentTemplate();
	public String getGoodAverageTemplate();
}