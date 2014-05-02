package com.adiguzel.shopr.explanation;

import com.adiguzel.shopr.explanation.model.AbstractExplanation;
import com.adiguzel.shopr.explanation.model.Explanation;

public interface SurfaceGenerator {
	
	public Explanation transform(AbstractExplanation abstractExplanation);
	
}
