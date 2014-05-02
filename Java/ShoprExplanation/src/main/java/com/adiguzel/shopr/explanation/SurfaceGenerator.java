package com.adiguzel.shopr.explanation;

import com.adiguzel.shopr.explanation.model.AbstractExplanation;

public interface SurfaceGenerator {
	
	public CharSequence transform(AbstractExplanation abstractExplanation);

}
